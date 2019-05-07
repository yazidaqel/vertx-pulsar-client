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

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.vertx.ext.pulsar.PulsarConsumerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.pulsar.PulsarConsumerOptions} original class using Vert.x codegen.
 */
public class PulsarConsumerOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PulsarConsumerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "ackTimeout":
          if (member.getValue() instanceof Number) {
            obj.setAckTimeout(((Number)member.getValue()).longValue());
          }
          break;
        case "subscriptionName":
          if (member.getValue() instanceof String) {
            obj.setSubscriptionName((String)member.getValue());
          }
          break;
        case "subscriptionType":
          if (member.getValue() instanceof String) {
            obj.setSubscriptionType((String)member.getValue());
          }
          break;
        case "topic":
          if (member.getValue() instanceof String) {
            obj.setTopic((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(PulsarConsumerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PulsarConsumerOptions obj, java.util.Map<String, Object> json) {
    json.put("ackTimeout", obj.getAckTimeout());
    if (obj.getSubscriptionName() != null) {
      json.put("subscriptionName", obj.getSubscriptionName());
    }
    if (obj.getSubscriptionType() != null) {
      json.put("subscriptionType", obj.getSubscriptionType());
    }
    if (obj.getTopic() != null) {
      json.put("topic", obj.getTopic());
    }
  }
}
