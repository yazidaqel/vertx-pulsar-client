package io.vertx.ext.pulsar.impl;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.pulsar.PulsarConnection;
import io.vertx.ext.pulsar.PulsarMessage;
import io.vertx.ext.pulsar.PulsarProducer;

public class PulsarProducerImpl implements PulsarProducer {
  @Override
  public PulsarProducer send(PulsarMessage message) {
    return null;
  }

  @Override
  public PulsarProducer sendWithAck(PulsarMessage message, Handler<AsyncResult<Void>> acknowledgementHandler) {
    return null;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {

  }

  @Override
  public String address() {
    return null;
  }

  @Override
  public PulsarConnection connection() {
    return null;
  }

  @Override
  public WriteStream<PulsarMessage> exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public WriteStream<PulsarMessage> write(PulsarMessage message) {
    return null;
  }

  @Override
  public void end() {

  }

  @Override
  public WriteStream<PulsarMessage> setWriteQueueMaxSize(int i) {
    return null;
  }

  @Override
  public boolean writeQueueFull() {
    return false;
  }

  @Override
  public WriteStream<PulsarMessage> drainHandler(@Nullable Handler<Void> handler) {
    return null;
  }
}
