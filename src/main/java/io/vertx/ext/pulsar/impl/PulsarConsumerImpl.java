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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PulsarConsumerImpl implements PulsarConsumer {

  private final PulsarConnectionImpl connection;
  private String topic;
  private boolean closed;
  private AtomicLong demand = new AtomicLong();

  private Handler<Throwable> exceptionHandler;
  private Handler<PulsarMessage> handler;
  private Handler<Void> endHandler;
  private Consumer consumer;

  PulsarConsumerImpl(
    String topic,
    PulsarConnectionImpl connection,
    PulsarConsumerOptions options,
    Handler<PulsarMessage> handler,
    Handler<AsyncResult<PulsarConsumer>> completionHandler
  ) {
    this.topic = options.getTopic() != null ? options.getTopic() : topic;
    ;
    this.connection = connection;
    this.handler = handler;
    PulsarClient pulsarClient = connection.connection();
    try {
      ConsumerBuilder consumerBuilder = pulsarClient.newConsumer();
      consumerBuilder.topic(this.topic);
      consumerBuilder.ackTimeout(options.getAckTimeout(), TimeUnit.SECONDS);
      if (options.getSubscriptionName() != null)
        consumerBuilder.subscriptionName(options.getSubscriptionName());

      if (options.getSubscriptionType() != null)
        consumerBuilder.subscriptionType(SubscriptionType.valueOf(options.getSubscriptionType()));

      this.consumer = consumerBuilder.subscribe();

      completionHandler.handle(Future.succeededFuture(this));
      closed = false;
      demand.set(Long.MAX_VALUE);
      long v = demand.get();
      do {
        try {
          if (v == 0L)
            continue;
          // TODO: Add generic type for message and proceed with conversion
          if (this.handler != null) {
            Message<PulsarMessage> message = this.consumer.receive();
            this.handler.handle(message.getValue());
          }
        } catch (PulsarClientException e) {
          exceptionHandler.handle(e);
        }
      } while (!connection.isClosed() && !this.demand.compareAndSet(v, v-1));

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
  public PulsarConsumer handler(@Nullable Handler<PulsarMessage> handler) {
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
