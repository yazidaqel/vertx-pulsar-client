package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.DataObject;

@DataObject(generateConverter = true)
public class PulsarConsumerOptions {

  private String topic;
  private String subscriptionName;
  private long ackTimeout;
  private String subscriptionType;

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getSubscriptionName() {
    return subscriptionName;
  }

  public void setSubscriptionName(String subscriptionName) {
    this.subscriptionName = subscriptionName;
  }

  public long getAckTimeout() {
    return ackTimeout;
  }

  public void setAckTimeout(long ackTimeout) {
    this.ackTimeout = ackTimeout;
  }

  public String getSubscriptionType() {
    return subscriptionType;
  }

  public void setSubscriptionType(String subscriptionType) {
    this.subscriptionType = subscriptionType;
  }
}
