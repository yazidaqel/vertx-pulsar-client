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

import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.pulsar.*;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PulsarConnectionImpl implements PulsarConnection {

  private static final Logger logger = LoggerFactory.getLogger(PulsarConnectionImpl.class);

  private final AtomicBoolean closed = new AtomicBoolean();

  private final AtomicReference<PulsarClient> connection = new AtomicReference<>();

  private final List<PulsarConsumer> consumers = new CopyOnWriteArrayList<>();
  private final List<PulsarProducer> producers = new CopyOnWriteArrayList<>();
  private PulsarClientOptions options;
  private Context context;
  private Handler<Throwable> exceptionHandler;

  private String serviceUrl;

  PulsarConnectionImpl(Context context, PulsarClientImpl client, PulsarClientOptions options,
                       Handler<AsyncResult<PulsarConnection>> connectionHandler) {
    this.options = options;
    this.context = context;
    this.serviceUrl = "pulsar://" + this.options.getHost() + ":" + this.options.getPort();

    logger.debug("Connecting to = "+this.serviceUrl);

    runOnContext(x -> connect(client,
      Objects.requireNonNull(connectionHandler, "connection handler cannot be `null`"))
    );
  }

  private void connect(PulsarClientImpl client, Handler<AsyncResult<PulsarConnection>> connectionHandler) {
    try {
      logger.info("Calling connect");
      if (connection.get() != null && !closed.get()) {
        logger.debug("Already connected");
        connectionHandler.handle(Future.succeededFuture(this));
        return;
      }
      PulsarClient pulsarClient = PulsarClient.builder()
        .serviceUrl(this.serviceUrl)
        .build();
      logger.debug("Connected");
      connection.set(pulsarClient);
      client.register(this);
      closed.set(false);
      logger.info("Connected & Registered");
      connectionHandler.handle(Future.succeededFuture(this));
    } catch (Exception ex) {
      if (exceptionHandler != null)
        exceptionHandler.handle(ex);
      connectionHandler.handle(Future.failedFuture(ex));
    }
  }


  @Override
  public PulsarConnection exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public PulsarConnection close(Handler<AsyncResult<Void>> done) {

    context.runOnContext(ignored -> {
      PulsarClient pulsarClient = connection.get();
      if (pulsarClient == null || closed.get()) {
        done.handle(Future.failedFuture("No opening connection to close"));
        return;
      }
      try {
        CompletableFuture future = pulsarClient.closeAsync();
        future.whenComplete((r, e) -> {
          if (e == null) {
            done.handle(Future.succeededFuture());
          } else {
            done.handle(Future.failedFuture((Exception) e));
          }
        });
      } catch (Exception ex) {
        exceptionHandler.handle(ex);
      } finally {
        closed.set(true);
      }
    });
    return this;
  }

  @Override
  public PulsarConnection createConsumer(String topic, Class classSchema, PulsarConsumerOptions options, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    PulsarConsumerOptions pulsarConsumerOptions = options == null ? new PulsarConsumerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarConsumerImpl(topic, classSchema, this, pulsarConsumerOptions, null, completionHandler);
    });
    return this;
  }

  @Override
  public PulsarConnection createConsumer(String topic, PulsarConsumerOptions options, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    PulsarConsumerOptions pulsarConsumerOptions = options == null ? new PulsarConsumerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarConsumerImpl(topic, Schema.BYTES, this, pulsarConsumerOptions, null, completionHandler);
    });
    return this;
  }

  @Override
  public PulsarConnection createConsumer(String topic, PulsarConsumerOptions options, Handler<PulsarMessage> messageHandler, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    PulsarConsumerOptions pulsarConsumerOptions = options == null ? new PulsarConsumerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarConsumerImpl(topic, Schema.BYTES, this, pulsarConsumerOptions, messageHandler, completionHandler);
    });
    return this;
  }

  @Override
  public PulsarConnection createProducer(String topic, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return createProducer(topic, null, completionHandler);
  }

  @Override
  public PulsarConnection createProducer(String topic, PulsarProducerOptions options, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    PulsarProducerOptions pulsarProducerOptions = options == null ? new PulsarProducerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarProducerImpl(topic, Schema.BYTES, this, pulsarProducerOptions, completionHandler);
    });
    return this;
  }

  @Override
  public PulsarConnection createProducer(String topic, Class classSchema, PulsarProducerOptions options, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    PulsarProducerOptions pulsarProducerOptions = options == null ? new PulsarProducerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarProducerImpl(topic, classSchema, this, pulsarProducerOptions, completionHandler);
    });
    return this;
  }

  void runOnContext(Handler<Void> action) {
    context.runOnContext(action);
  }

  void runWithTrampoline(Handler<Void> action) {
    if (Vertx.currentContext() == context) {
      action.handle(null);
    } else {
      runOnContext(action);
    }
  }

  protected PulsarClient connection() {
    return connection.get();
  }

  protected boolean isClosed() {
    return closed.get();
  }

  protected void register(PulsarConsumer consumer) {
    consumers.add(consumer);
  }

  protected void register(PulsarProducer producer) {
    producers.add(producer);
  }

  protected void unregister(PulsarConsumer consumer) {
    consumers.remove(consumer);
  }

  protected void unregister(PulsarProducer producer) {
    producers.remove(producer);
  }
}
