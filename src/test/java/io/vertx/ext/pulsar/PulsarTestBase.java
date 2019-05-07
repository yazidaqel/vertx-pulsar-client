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
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

public class PulsarTestBase {

  PulsarClient pulsarClient;

  @ClassRule
  public static GenericContainer pulsar = new GenericContainer("apachepulsar/pulsar:2.0.1-incubating")
    .withExposedPorts(6650)
    .withExposedPorts(8080)
    .withCommand("bin/pulsar standalone", "-v", "$PWD/data:/pulsar/data")
    ;

  private Vertx vertx;
  String host;
  int port;

}
