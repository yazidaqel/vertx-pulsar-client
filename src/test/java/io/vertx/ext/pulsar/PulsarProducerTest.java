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

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(VertxUnitRunner.class)
public class PulsarProducerTest extends PulsarTestBase {

  private static final Logger logger = LoggerFactory.getLogger(PulsarProducerTest.class);

  @Test(timeout = 20000)
  public void testProducerWithoutOptions() {
    String topic = UUID.randomUUID().toString();
    String sentContent = "myMessageContent-" + topic;
    AtomicBoolean done = new AtomicBoolean();

    client = usage.getClient();
    client.connect(handler -> {
      logger.debug("Test connection result: "+handler != null);
      assertTrue(handler.succeeded());
      if (handler.succeeded()) {
        logger.debug("Test Succeeded to connect");
        PulsarConnection pulsarConnection = handler.result();
        pulsarConnection.createProducer(topic, p -> {
          logger.debug("Test Creating Producer");
          PulsarProducer pulsarProducer = p.result();
          PulsarMessage pulsarMessage = PulsarMessage.create(new JsonObject().put("test", "test"));
          pulsarProducer.send(pulsarMessage);

        });
      } else {
        logger.error("Test Failed to connect", handler.cause());

      }
      done.set(true);
    });
    await().untilAtomic(done, is(true));

  }
}
