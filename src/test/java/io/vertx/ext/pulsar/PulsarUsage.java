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
