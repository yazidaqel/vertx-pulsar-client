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

@DataObject(generateConverter = true)
public class PulsarProducerOptions {

  private String producerName;
  private String topic;
  private int sendTimeout;
  private boolean enableBatching;
  private String 	compressionType;
  private boolean blockIfQueueFull;
  private long 	batchingMaxPublishDelay;
  private int batchingMaxMessages;

  public PulsarProducerOptions(){
    super();
  }
  public PulsarProducerOptions(JsonObject jsonObject){
    super();
  }

  public String getProducerName() {
    return producerName;
  }

  public void setProducerName(String producerName) {
    this.producerName = producerName;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public int getSendTimeout() {
    return sendTimeout;
  }

  public void setSendTimeout(int sendTimeout) {
    this.sendTimeout = sendTimeout;
  }

  public boolean isEnableBatching() {
    return enableBatching;
  }

  public void setEnableBatching(boolean enableBatching) {
    this.enableBatching = enableBatching;
  }

  public String getCompressionType() {
    return compressionType;
  }

  public void setCompressionType(String compressionType) {
    this.compressionType = compressionType;
  }

  public boolean isBlockIfQueueFull() {
    return blockIfQueueFull;
  }

  public void setBlockIfQueueFull(boolean blockIfQueueFull) {
    this.blockIfQueueFull = blockIfQueueFull;
  }

  public long getBatchingMaxPublishDelay() {
    return batchingMaxPublishDelay;
  }

  public void setBatchingMaxPublishDelay(long batchingMaxPublishDelay) {
    this.batchingMaxPublishDelay = batchingMaxPublishDelay;
  }

  public int getBatchingMaxMessages() {
    return batchingMaxMessages;
  }

  public void setBatchingMaxMessages(int batchingMaxMessages) {
    this.batchingMaxMessages = batchingMaxMessages;
  }
}
