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
package io.vertx.ext.pulsar.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.pulsar.PulsarMessage;

// TODO: Manage pulsar messages and choose a generic format for communication.
// bytes can be used or a generic schema where the body is always bytes using JsonObject conversion
public class PulsarMessageImpl implements PulsarMessage {

  @Override
  public String topic() {
    return null;
  }

  @Override
  public boolean bodyAsBoolean() {
    return false;
  }

  @Override
  public byte bodyAsByte() {
    return 0;
  }

  @Override
  public short bodyAsShort() {
    return 0;
  }

  @Override
  public int bodyAsInteger() {
    return 0;
  }

  @Override
  public long bodyAsLong() {
    return 0;
  }

  @Override
  public float bodyAsFloat() {
    return 0;
  }

  @Override
  public double bodyAsDouble() {
    return 0;
  }

  @Override
  public char bodyAsChar() {
    return 0;
  }

  @Override
  public Buffer bodyAsBinary() {
    return null;
  }

  @Override
  public String bodyAsString() {
    return null;
  }

  @Override
  public JsonObject bodyAsJsonObject() {
    return null;
  }

  @Override
  public JsonArray bodyAsJsonArray() {
    return null;
  }
}
