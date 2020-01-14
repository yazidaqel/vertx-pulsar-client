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
 * Converter and mapper for {@link io.vertx.ext.pulsar.PulsarProducerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.pulsar.PulsarProducerOptions} original class using Vert.x codegen.
 */
public class PulsarProducerOptionsConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PulsarProducerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "batchingMaxMessages":
          if (member.getValue() instanceof Number) {
            obj.setBatchingMaxMessages(((Number)member.getValue()).intValue());
          }
          break;
        case "batchingMaxPublishDelay":
          if (member.getValue() instanceof Number) {
            obj.setBatchingMaxPublishDelay(((Number)member.getValue()).longValue());
          }
          break;
        case "blockIfQueueFull":
          if (member.getValue() instanceof Boolean) {
            obj.setBlockIfQueueFull((Boolean)member.getValue());
          }
          break;
        case "compressionType":
          if (member.getValue() instanceof String) {
            obj.setCompressionType((String)member.getValue());
          }
          break;
        case "enableBatching":
          if (member.getValue() instanceof Boolean) {
            obj.setEnableBatching((Boolean)member.getValue());
          }
          break;
        case "producerName":
          if (member.getValue() instanceof String) {
            obj.setProducerName((String)member.getValue());
          }
          break;
        case "sendTimeout":
          if (member.getValue() instanceof Number) {
            obj.setSendTimeout(((Number)member.getValue()).intValue());
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

  public static void toJson(PulsarProducerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PulsarProducerOptions obj, java.util.Map<String, Object> json) {
    json.put("batchingMaxMessages", obj.getBatchingMaxMessages());
    json.put("batchingMaxPublishDelay", obj.getBatchingMaxPublishDelay());
    json.put("blockIfQueueFull", obj.isBlockIfQueueFull());
    if (obj.getCompressionType() != null) {
      json.put("compressionType", obj.getCompressionType());
    }
    json.put("enableBatching", obj.isEnableBatching());
    if (obj.getProducerName() != null) {
      json.put("producerName", obj.getProducerName());
    }
    json.put("sendTimeout", obj.getSendTimeout());
    if (obj.getTopic() != null) {
      json.put("topic", obj.getTopic());
    }
  }
}
