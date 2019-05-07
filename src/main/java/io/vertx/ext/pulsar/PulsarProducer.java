package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;

@VertxGen
public interface PulsarProducer extends WriteStream<PulsarMessage> {

  PulsarProducer send(PulsarMessage message);

  void close(Handler<AsyncResult<Void>> handler);

  String topic();

  PulsarConnection connection();
}
