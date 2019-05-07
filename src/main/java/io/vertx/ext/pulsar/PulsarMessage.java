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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.pulsar.impl.PulsarMessageImpl;

@VertxGen
public interface PulsarMessage {

  static PulsarMessage create() {
    return new PulsarMessageImpl();
  }

  String topic();

  boolean bodyAsBoolean();

  byte bodyAsByte();

  short bodyAsShort();

  int bodyAsInteger();

  long bodyAsLong();

  float bodyAsFloat();

  double bodyAsDouble();

  char bodyAsChar();

  Buffer bodyAsBinary();

  String bodyAsString();

  JsonObject bodyAsJsonObject();

  JsonArray bodyAsJsonArray();

}
