package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@VertxGen
public interface PulsarConnection {

  @Fluent
  PulsarConnection exceptionHandler(Handler<Throwable> handler);

  @Fluent
  PulsarConnection close(Handler<AsyncResult<Void>> done);

  @Fluent
  PulsarConnection createConsumer(String address, Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarConnection createConsumer(String address, PulsarConsumerOptions options,
                                  Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarConnection createProducer(String address, Handler<AsyncResult<PulsarProducer>> completionHandler);

  @Fluent
  PulsarConnection createProducer(String address, PulsarProducerOptions options,
                                  Handler<AsyncResult<PulsarProducer>> completionHandler);

}
