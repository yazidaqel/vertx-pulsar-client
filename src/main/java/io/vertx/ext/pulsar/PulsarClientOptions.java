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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, inheritConverter = true)
public class PulsarClientOptions {

  private String host = "localhost";
  private int port = 6650;

  public PulsarClientOptions() {
    super();
  }

  public PulsarClientOptions(PulsarClientOptions other) {
    this.host = other.host;
    this.port = other.port;
  }

  public PulsarClientOptions(JsonObject jsonObject) {

  }

  public String getHost() {
    return host;
  }

  public PulsarClientOptions setHost(String host) {
    this.host = host;
    return this;
  }

  public int getPort() {
    return port;
  }

  public PulsarClientOptions setPort(int port) {
    this.port = port;
    return this;
  }
}
