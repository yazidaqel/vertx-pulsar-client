/*
 * Copyright (c) 2018-2019 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *        The Eclipse Public License is available at
 *        http://www.eclipse.org/legal/epl-v10.html
 *
 *        The Apache License v2.0 is available at
 *        http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
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
  PulsarConnection createConsumer(String address,
                                  PulsarConsumerOptions options,
                                  Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarConnection createConsumer(String address,
                                  PulsarConsumerOptions options,
                                  Handler<PulsarMessage> messageHandler,
                                  Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @GenIgnore
  PulsarConnection createConsumer(String topic,
                                  Class classSchema,
                                  PulsarConsumerOptions options,
                                  Handler<AsyncResult<PulsarConsumer>> completionHandler);

  @Fluent
  PulsarConnection createProducer(String address,
                                  Handler<AsyncResult<PulsarProducer>> completionHandler);

  @Fluent
  PulsarConnection createProducer(String address,
                                  PulsarProducerOptions options,
                                  Handler<AsyncResult<PulsarProducer>> completionHandler);

  @GenIgnore
  public PulsarConnection createProducer(String topic,
                                         Class classSchema,
                                         PulsarProducerOptions options,
                                         Handler<AsyncResult<PulsarProducer>> completionHandler);

}
