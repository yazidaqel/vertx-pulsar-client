package io.vertx.ext.pulsar;

import io.vertx.core.Vertx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

public class ConnectionTest extends PulsarTestBase {

  private Vertx vertx;

  @Before
  private void init() {
    vertx = Vertx.vertx();
  }
  @After
  public void destroy() {
    vertx.close();
  }

  @Test
  public void testConnectionSuccessWithDetailsPassedInOptions() {
    AtomicBoolean done = new AtomicBoolean();
    pulsarClient = PulsarClient.create(new PulsarClientOptions()
      .setHost(host)
      .setPort(port)
    ).connect(
      ar -> done.set(ar.succeeded())
    );

    await().untilAtomic(done, is(true));
  }


}
