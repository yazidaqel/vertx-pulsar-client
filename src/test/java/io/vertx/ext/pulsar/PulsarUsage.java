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

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PulsarUsage {
  private static Logger LOGGER = LoggerFactory.getLogger(PulsarUsage.class);

  private final Context context;
  private PulsarClient client;
  private PulsarConnection connection;

  private List<PulsarProducer> senders = new CopyOnWriteArrayList<>();
  private List<PulsarConsumer> receivers = new CopyOnWriteArrayList<>();

  public PulsarUsage(Vertx vertx, String host, int port) {
    CountDownLatch latch = new CountDownLatch(1);
    this.context = vertx.getOrCreateContext();
    context.runOnContext(x -> {
      client = PulsarClient.create(vertx, null);
      client.connect(handler -> {
        if (handler.succeeded()) {
          LOGGER.info("Connection to the Pulsar succeeded");
          this.connection = handler.result();
        } else {

        }
        latch.countDown();
      });
    });
    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  public void close() throws InterruptedException {
    CountDownLatch entities = new CountDownLatch(senders.size() + receivers.size());
    context.runOnContext(ignored -> {
      senders.forEach(sender -> {
          entities.countDown();
      });
      receivers.forEach(receiver -> {
          entities.countDown();
      });
    });

    entities.await(30, TimeUnit.SECONDS);

    if (connection != null) {
      CountDownLatch latch = new CountDownLatch(1);
      context.runOnContext(n -> connection.close(x -> latch.countDown()));
      latch.await(10, TimeUnit.SECONDS);
    }


  }
}
