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
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.pulsar.PulsarConnection;
import io.vertx.ext.pulsar.PulsarConsumer;
import io.vertx.ext.pulsar.PulsarConsumerOptions;
import io.vertx.ext.pulsar.PulsarMessage;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PulsarConsumerImpl<T> implements PulsarConsumer<T> {

  private final PulsarConnectionImpl connection;
  private final String topic;
  private boolean closed;
  private AtomicLong demand = new AtomicLong();

  private Handler<Throwable> exceptionHandler;
  private Handler<PulsarMessage<T>> handler;
  private Handler<Void> endHandler;
  private Consumer consumer;

  protected PulsarConsumerImpl(
    String topic,
    Class<T> classSchema,
    PulsarConnectionImpl connection,
    PulsarConsumerOptions options,
    Handler<PulsarMessage<T>> handler,
    Handler<AsyncResult<PulsarConsumer>> completionHandler
  ) {
    Schema<T> schema = JSONSchema.of(classSchema);
    this.topic = options.getTopic() != null ? options.getTopic() : topic;
    this.connection = connection;
    this.handler = handler;
    init(
      schema,
      options,
      completionHandler
    );
  }
  protected PulsarConsumerImpl(
    String topic,
    Schema schema,
    PulsarConnectionImpl connection,
    PulsarConsumerOptions options,
    Handler<PulsarMessage<T>> handler,
    Handler<AsyncResult<PulsarConsumer>> completionHandler
  ) {
    this.topic = options.getTopic() != null ? options.getTopic() : topic;
    this.connection = connection;
    this.handler = handler;
    init(
      schema,
      options,
      completionHandler
    );
  }

  void init(Schema schema,
       PulsarConsumerOptions options,
       Handler<AsyncResult<PulsarConsumer>> completionHandler){

    PulsarClient pulsarClient = connection.connection();
    try {
      ConsumerBuilder consumerBuilder = pulsarClient.newConsumer(schema);
      consumerBuilder.topic(this.topic);
      consumerBuilder.ackTimeout(options.getAckTimeout(), TimeUnit.SECONDS);
      if (options.getSubscriptionName() != null)
        consumerBuilder.subscriptionName(options.getSubscriptionName());

      if (options.getSubscriptionType() != null)
        consumerBuilder.subscriptionType(SubscriptionType.valueOf(options.getSubscriptionType()));

      this.consumer = consumerBuilder.subscribe();
      this.connection.register(this);
      completionHandler.handle(Future.succeededFuture(this));
      closed = false;
      demand.set(Long.MAX_VALUE);
      long v = demand.get();
      do {
        try {
          if (v == 0L)
            continue;
          if (this.handler != null) {
            Message<T> message = this.consumer.receive();
            this.handler.handle(new PulsarMessageImpl<T>(message.getValue()));
            this.consumer.acknowledge(message);
          }
        } catch (PulsarClientException e) {
          if (exceptionHandler != null)
            exceptionHandler.handle(e);
        }
      } while (!connection.isClosed() && !this.demand.compareAndSet(v, v - 1));

    } catch (Exception e) {
      completionHandler.handle(Future.failedFuture(e));
    }

  }



  @Override
  public String topic() {
    return this.topic;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    synchronized (this) {
      if (closed) {
        handler.handle(Future.succeededFuture());
        return;
      }
      closed = true;
    }
    connection.unregister(this);
    connection.runWithTrampoline(x -> {
      try {
        consumer.close();
        handler.handle(Future.succeededFuture());
      } catch (PulsarClientException e) {
        handler.handle(Future.failedFuture(e));
      }
    });

  }

  @Override
  public PulsarConnection connection() {
    return connection;
  }

  @Override
  public PulsarConsumer exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public PulsarConsumer<T> handler(@Nullable Handler<PulsarMessage<T>> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public PulsarConsumer pause() {
    demand.set(0L);
    return this;
  }

  @Override
  public synchronized PulsarConsumer resume() {
    return fetch(Long.MAX_VALUE);
  }

  @Override
  public synchronized PulsarConsumer fetch(long amount) {
    if (amount > 0) {
      if (demand.addAndGet(amount) < 0L) {
        demand.set(Long.MAX_VALUE);
      }
    }
    return this;
  }

  @Override
  public PulsarConsumer endHandler(@Nullable Handler<Void> handler) {
    this.endHandler = handler;
    return this;
  }
}
