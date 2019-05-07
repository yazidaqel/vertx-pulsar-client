/*
 * Copyright (c) 2018-2019 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *        The Eclipse Public License is available at
 *        http://www.eclipse.org/legal/epl-v10.html
 *
 *        The Apache License v2.0 is available at
 *        http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.pulsar.impl;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.ext.pulsar.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class PulsarClientImpl implements PulsarClient {

  private final Vertx vertx;
  private final PulsarClientOptions options;

  private final boolean mustCloseVertxOnClose;

  private final List<PulsarConnection> connections = new CopyOnWriteArrayList<>();

  public PulsarClientImpl(Vertx vertx, PulsarClientOptions options, boolean mustCloseVertxOnClose) {
    this.vertx = vertx;
    if (options == null) {
      this.options = new PulsarClientOptions();
    } else {
      this.options = options;
    }
    this.mustCloseVertxOnClose = mustCloseVertxOnClose;
  }

  @Override
  public PulsarClient connect(Handler<AsyncResult<PulsarConnection>> connectionHandler) {
    Objects.requireNonNull(options.getHost(), "Host must be set");
    Objects.requireNonNull(connectionHandler, "Handler must not be null");
    new PulsarConnectionImpl(vertx.getOrCreateContext(), this, options, connectionHandler);
    return this;
  }

  @Override
  public void close(@Nullable Handler<AsyncResult<Void>> handler) {
    List<Future> actions = new ArrayList<>();
    for (PulsarConnection connection : connections) {
      Future<Void> future = Future.future();
      connection.close(future);
      actions.add(future);
    }

    CompositeFuture.join(actions).setHandler(done -> {
      connections.clear();
      if (mustCloseVertxOnClose) {
        vertx.close(x -> {
          if (done.succeeded() && x.succeeded()) {
            if (handler != null) {
              handler.handle(Future.succeededFuture());
            }
          } else {
            if (handler != null) {
              handler.handle(Future.failedFuture(done.failed() ? done.cause() : x.cause()));
            }
          }
        });
      } else if (handler != null) {
        handler.handle(done.mapEmpty());
      }
    });
  }

  @Override
  public PulsarClient createConsumer(String address, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
      return connect(res -> {
        if (res.failed()) {
          completionHandler.handle(res.mapEmpty());
        } else {
          res.result().createConsumer(address, completionHandler);
        }
      });
  }

  @Override
  public PulsarClient createConsumer(String address, Handler<PulsarMessage> messageHandler, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return connect(res -> {
      if (res.failed()) {
        completionHandler.handle(res.mapEmpty());
      } else {
        res.result().createConsumer(address, messageHandler, completionHandler);
      }
    });
  }

  @Override
  public PulsarClient createConsumer(String address, PulsarConsumerOptions options, Handler<PulsarMessage> messageHandler, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return connect(res -> {
      if (res.failed()) {
        completionHandler.handle(res.mapEmpty());
      } else {
        res.result().createConsumer(address,options, messageHandler, completionHandler);
      }
    });
  }

  @Override
  public PulsarClient createProducer(String address, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return connect(res -> {
      if (res.failed()) {
        completionHandler.handle(res.mapEmpty());
      } else {
        res.result().createProducer(address, completionHandler);
      }
    });
  }

  @Override
  public PulsarClient createProducer(String address, PulsarProducerOptions options, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return connect(res -> {
      if (res.failed()) {
        completionHandler.handle(res.mapEmpty());
      } else {
        res.result().createProducer(address, options,completionHandler);
      }
    });
  }

  synchronized void register(PulsarConnectionImpl connection) {
    connections.add(connection);
  }
}
