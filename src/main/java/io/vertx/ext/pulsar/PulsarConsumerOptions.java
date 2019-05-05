package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.DataObject;

@DataObject(generateConverter = true)
public class PulsarConsumerOptions {

  private String topic;
  private String subscriptionName;
  private long ackTimeout;
  private String subscriptionType;
}
