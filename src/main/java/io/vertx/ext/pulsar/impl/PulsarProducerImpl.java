package io.vertx.ext.pulsar.impl;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.pulsar.PulsarConnection;
import io.vertx.ext.pulsar.PulsarMessage;
import io.vertx.ext.pulsar.PulsarProducer;
import io.vertx.ext.pulsar.PulsarProducerOptions;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PulsarProducerImpl implements PulsarProducer {

  private final PulsarConnectionImpl connection;
  private final PulsarProducerOptions options;
  private final String topic;
  private AtomicBoolean closed;
  private Handler<Throwable> exceptionHandler;
  private Handler<Void> drainHandler;
  private Producer producer;


  PulsarProducerImpl(
    String topic,
    PulsarConnectionImpl connection,
    PulsarProducerOptions options,
    Handler<AsyncResult<PulsarProducer>> completionHandler
  ) {
    this.connection = connection;
    this.options = options;
    this.topic = options.getTopic() != null ? options.getTopic() : topic;
    PulsarClient pulsarClient = this.connection.connection();
    try {
      ProducerBuilder producerBuilder = pulsarClient.newProducer();
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
      completionHandler.handle(Future.failedFuture(ex));
    }
  }

  @Override
  public PulsarProducer send(PulsarMessage message) {
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
    connection.runWithTrampoline(x->{
      try {
        producer.close();
        actualHandler.handle(Future.succeededFuture());
      }catch (Exception ex){
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
  public PulsarProducer exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public PulsarProducer write(PulsarMessage message) {
    return doSend(message, null);
  }

  @Override
  public void end() {

  }

  @Override
  public PulsarProducer setWriteQueueMaxSize(int i) {
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return false;
  }

  @Override
  public PulsarProducer drainHandler(@Nullable Handler<Void> handler) {
    this.drainHandler = handler;
    return this;
  }

  private PulsarProducer doSend(PulsarMessage message, Handler<AsyncResult<Void>> acknowledgmentHandler) {

    synchronized (this) {
      //      remoteCredit--;
    }

    connection.runWithTrampoline(x -> {
      try {
        MessageId messageId = producer.send(message);
        // Check how pulsar notify the producer about message delivery.

      } catch (PulsarClientException e) {
        acknowledgmentHandler.handle(Future.failedFuture(e));
      }

    });
    return this;
  }
}
