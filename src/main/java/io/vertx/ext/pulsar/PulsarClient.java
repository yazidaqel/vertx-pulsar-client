package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.pulsar.impl.PulsarClientImpl;

import java.util.Objects;

@VertxGen
public interface PulsarClient {

  static PulsarClient create(PulsarClientOptions options){return new PulsarClientImpl(Vertx.vertx(), options, true); }

  static PulsarClient create(Vertx vertx, PulsarClientOptions options) {
    return new PulsarClientImpl(Objects.requireNonNull(vertx), options, false);
  }

  @Fluent
  PulsarClient connect(Handler<AsyncResult<PulsarConnection>> connectionHandler);

  void close(@Nullable Handler<AsyncResult<Void>> closeHandler);

  @Fluent
  PulsarClient createConsumer(String address, Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarClient createConsumer(String address, Handler<PulsarMessage> messageHandler,
                            Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarClient createConsumer(String address, PulsarConsumerOptions options,
                            Handler<PulsarMessage> messageHandler,
                            Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarProducer createProducer(String address, Handler<AsyncResult<PulsarProducer>> completionHandler);

  @Fluent
  PulsarProducer createProducer(String address, PulsarProducerOptions options,
                                Handler<AsyncResult<PulsarProducer>> completionHandler);

}
