package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.pulsar.impl.PulsarMessageBuilderImpl;

@VertxGen
public interface PulsarMessage {

  static PulsarMessageBuilder create() {
    return new PulsarMessageBuilderImpl();
  }

  static PulsarMessageBuilder create(PulsarMessage existing) {
    return new PulsarMessageBuilderImpl(existing);
  }


}
