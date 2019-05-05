package io.vertx.ext.pulsar;

import io.vertx.codegen.annotations.DataObject;

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

  public void setPort(int port) {
    this.port = port;
  }
}
