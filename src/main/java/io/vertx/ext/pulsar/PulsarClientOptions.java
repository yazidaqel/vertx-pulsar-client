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
