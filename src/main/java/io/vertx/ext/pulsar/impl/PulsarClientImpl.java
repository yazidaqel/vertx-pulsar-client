package io.vertx.ext.pulsar.impl;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.pulsar.*;

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
  public void close(@Nullable Handler<AsyncResult<Void>> closeHandler) {

  }

  @Override
  public PulsarClient createConsumer(String address, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return null;
  }

  @Override
  public PulsarClient createConsumer(String address, Handler<PulsarMessage> messageHandler, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return null;
  }

  @Override
  public PulsarClient createConsumer(String address, PulsarConsumerOptions options, Handler<PulsarMessage> messageHandler, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return null;
  }

  @Override
  public PulsarProducer createProducer(String address, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return null;
  }

  @Override
  public PulsarProducer createProducer(String address, PulsarProducerOptions options, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return null;
  }

  synchronized void register(PulsarConnectionImpl connection) {
    connections.add(connection);
  }
}
