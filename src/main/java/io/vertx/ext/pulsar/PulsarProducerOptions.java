package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.DataObject;

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
