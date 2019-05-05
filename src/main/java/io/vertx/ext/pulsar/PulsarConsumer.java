package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

@VertxGen
public interface PulsarConsumer extends ReadStream<PulsarMessage> {

  @CacheReturn
  String topic();

  void close(Handler<AsyncResult<Void>> handler);

  PulsarConnection connection();
}
