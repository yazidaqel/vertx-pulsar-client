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
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PulsarTestBase {
  private static final Logger logger = LoggerFactory.getLogger(PulsarConnectionTest.class);


  PulsarClient client;

  @ClassRule
  public static PulsarContainer pulsar = new PulsarContainer(DockerImageName.parse("apachepulsar/pulsar:3.0.0"))
    .withCommand("-it")
    .withExposedPorts(6650,8080)
    .withCommand("bin/pulsar standalone");

  private Vertx vertx;
  String host;
  int port;
  PulsarUsage usage;

  @Before
  public void setup() {
    vertx = Vertx.vertx();
    host = pulsar.getHost();
    port = pulsar.getMappedPort(6650);
    System.setProperty("pulsar-host", host);
    System.setProperty("pulsar-port", Integer.toString(port));
    usage = new PulsarUsage(vertx, host, port);
  }

  @After
  public void tearDown() throws InterruptedException {
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);

    if (client != null) {
      client.close(x -> latch1.countDown());
      latch1.await(10, TimeUnit.SECONDS);
    }

    System.clearProperty("pulsar-host");
    System.clearProperty("pulsar-port");

    usage.close();
    vertx.close().onComplete(x -> latch2.countDown());

    latch2.await(10, TimeUnit.SECONDS);
  }

  public PulsarClient getClient() {
    return client;
  }

}
