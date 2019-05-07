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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

public class ConnectionTest extends PulsarTestBase {

  private Vertx vertx;

  @Before
  private void init() {
    vertx = Vertx.vertx();
  }
  @After
  public void destroy() {
    vertx.close();
  }

  @Test
  public void testConnectionSuccessWithDetailsPassedInOptions() {
    AtomicBoolean done = new AtomicBoolean();
    pulsarClient = PulsarClient.create(new PulsarClientOptions()
      .setHost(host)
      .setPort(port)
    ).connect(
      ar -> done.set(ar.succeeded())
    );

    await().untilAtomic(done, is(true));
  }


}
