package io.vertx.ext.pulsar.impl;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.pulsar.PulsarConnection;
import io.vertx.ext.pulsar.PulsarConsumer;
import io.vertx.ext.pulsar.PulsarConsumerOptions;
import io.vertx.ext.pulsar.PulsarMessage;

public class PulsarConsumerImpl implements PulsarConsumer {

  private String topic;
  private final PulsarConnectionImpl connection;
  private boolean closed;

  private Handler<Throwable> exceptionHandler;
  private Handler<PulsarMessage> handler;

  PulsarConsumerImpl(
    String topic,
    PulsarConnectionImpl connection,
    PulsarConsumerOptions options,
    Handler<PulsarMessage> handler, Handler<AsyncResult<PulsarConsumer>> completionHandler
  ){
    this.topic = topic;
    this.connection = connection;
    this.handler = handler;

  }

  @Override
  public String topic() {
    return null;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {

  }

  @Override
  public PulsarConnection connection() {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> handler(@Nullable Handler<PulsarMessage> handler) {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> pause() {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> resume() {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> fetch(long l) {
    return null;
  }

  @Override
  public ReadStream<PulsarMessage> endHandler(@Nullable Handler<Void> handler) {
    return null;
  }
}
