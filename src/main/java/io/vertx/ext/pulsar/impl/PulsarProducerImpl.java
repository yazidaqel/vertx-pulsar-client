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
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.pulsar.PulsarConnection;
import io.vertx.ext.pulsar.PulsarMessage;
import io.vertx.ext.pulsar.PulsarProducer;
import io.vertx.ext.pulsar.PulsarProducerOptions;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PulsarProducerImpl<T> implements PulsarProducer<T> {

  private static final Logger logger = LoggerFactory.getLogger(PulsarProducerImpl.class);

  private PulsarConnectionImpl connection;
  private PulsarProducerOptions options;
  private String topic;
  private AtomicBoolean closed;
  private Handler<Throwable> exceptionHandler;
  private Producer<T> producer;


  PulsarProducerImpl(
    String topic,
    Class<T> classSchema,
    PulsarConnectionImpl connection,
    PulsarProducerOptions options,
    Handler<AsyncResult<PulsarProducer<T>>> completionHandler
  ) {
    Schema<T> schema = JSONSchema.of(classSchema);
    new PulsarProducerImpl(topic, schema, connection, options, completionHandler);
  }

  PulsarProducerImpl(
    String topic,
    Schema<T> schema,
    PulsarConnectionImpl connection,
    PulsarProducerOptions options,
    Handler<AsyncResult<PulsarProducer<T>>> completionHandler
  ) {
    this.connection = connection;
    this.options = options;
    this.topic = options.getTopic() != null ? options.getTopic() : topic;
    PulsarClient pulsarClient = this.connection.connection();
    try {
      ProducerBuilder<T> producerBuilder = pulsarClient.newProducer(schema);
      producerBuilder.topic(this.topic);
      if (this.options.getProducerName() != null)
        producerBuilder.producerName(this.options.getProducerName());

      if (this.options.isEnableBatching()) {
        producerBuilder.enableBatching(true);
        if (this.options.getBatchingMaxMessages() > 0)
          producerBuilder.batchingMaxMessages(this.options.getBatchingMaxMessages());

        if (this.options.getBatchingMaxPublishDelay() > 0)
          producerBuilder.batchingMaxPublishDelay(this.options.getBatchingMaxPublishDelay(), TimeUnit.SECONDS);
      }

      if (this.options.getCompressionType() != null)
        producerBuilder.compressionType(CompressionType.valueOf(this.options.getCompressionType()));

      if (this.options.getSendTimeout() > 0)
        producerBuilder.sendTimeout(this.options.getSendTimeout(), TimeUnit.SECONDS);

      this.producer = producerBuilder.create();

      this.connection.register(this);

      completionHandler.handle(Future.succeededFuture(this));
    } catch (Exception ex) {
      if (exceptionHandler != null)
        exceptionHandler.handle(ex);
      completionHandler.handle(Future.failedFuture(ex));
    }
  }

  @Override
  public PulsarProducer<T> send(PulsarMessage<T> message) {
    return doSend(message, null);
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    Handler<AsyncResult<Void>> actualHandler;
    if (handler == null) {
      actualHandler = x -> { /* NOOP */ };
    } else {
      actualHandler = handler;
    }
    synchronized (this) {
      if (closed.get()) {
        actualHandler.handle(Future.succeededFuture());
        return;
      }
      closed.set(false);
    }
    connection.unregister(this);
    connection.runWithTrampoline(x -> {
      try {
        producer.close();
        actualHandler.handle(Future.succeededFuture());
      } catch (Exception ex) {
        actualHandler.handle(Future.failedFuture(ex));
      }
    });
  }

  @Override
  public String topic() {
    return this.topic;
  }

  @Override
  public PulsarConnection connection() {
    return this.connection;
  }

  @Override
  public PulsarProducer<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public Future<Void> write(PulsarMessage<T> pulsarMessage) {
    Promise<Void> promise = Promise.promise();
    doSend(pulsarMessage, promise);
    return promise.future();
  }

  @Override
  public Future<Void> end() {
    return null;
  }

  @Override
  public Future<Void> end(PulsarMessage<T> data) {
    return PulsarProducer.super.end(data);
  }


  @Override
  public PulsarProducer<T> setWriteQueueMaxSize(int i) {
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return false;
  }

  @Override
  public PulsarProducer<T> drainHandler(@Nullable Handler<Void> handler) {
    return this;
  }

  private PulsarProducer<T> doSend(PulsarMessage<T> message, Handler<AsyncResult<Void>> acknowledgmentHandler) {

    connection.runWithTrampoline(x -> {
      try {
        MessageId messageId = producer.send(message.body());
        logger.debug("Message sent, ID: " + messageId);
        acknowledgmentHandler.handle(
          Future.succeededFuture()
        );

      } catch (PulsarClientException e) {
        acknowledgmentHandler.handle(Future.failedFuture(e));
      }

    });
    return this;
  }
}
