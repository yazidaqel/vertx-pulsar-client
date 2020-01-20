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

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class PulsarConnectionTest extends PulsarTestBase {

  private static final Logger logger = LoggerFactory.getLogger(PulsarConnectionTest.class);

  private Vertx vertx;

  @Before
  public void init() {
    vertx = Vertx.vertx();
  }

  @After
  public void destroy() {
    vertx.close();
  }

  @Test
  public void testConnectionSuccessWithDetailsPassedInOptions() {
    AtomicBoolean done = new AtomicBoolean();
    logger.debug("testConnectionSuccessWithDetailsPassedInOptions");
    client = PulsarClient.create(new PulsarClientOptions()
      .setHost(host)
      .setPort(port)
    ).connect(
      handler -> {
        if(handler.succeeded()){
          client.close(closeHandler ->{
            done.set(closeHandler.succeeded());
            done.set(true);
          });
        }else
        done.set(true);
      }
    );

    await().untilAtomic(done, is(true));
  }

}
