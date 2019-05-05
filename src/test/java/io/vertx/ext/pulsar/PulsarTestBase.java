package io.vertx.ext.pulsar;

import io.vertx.core.Vertx;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

public class PulsarTestBase {

  PulsarClient pulsarClient;

  @ClassRule
  public static GenericContainer pulsar = new GenericContainer("apachepulsar/pulsar:2.0.1-incubating")
    .withExposedPorts(6650)
    .withExposedPorts(8080)
    .withCommand("bin/pulsar standalone", "-v", "$PWD/data:/pulsar/data")
    ;

  private Vertx vertx;
  String host;
  int port;

}
