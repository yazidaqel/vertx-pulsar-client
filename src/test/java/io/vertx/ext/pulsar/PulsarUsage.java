package io.vertx.ext.pulsar;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.util.concurrent.CountDownLatch;

public class PulsarUsage {

  private final Context context;

  public PulsarUsage(Vertx vertx, String host, int port) {
    CountDownLatch latch = new CountDownLatch(1);
    this.context = vertx.getOrCreateContext();
    context.runOnContext(x -> {
      
    });
    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
