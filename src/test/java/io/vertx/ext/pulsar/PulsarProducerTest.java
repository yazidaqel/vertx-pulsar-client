package io.vertx.ext.pulsar;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class PulsarProducerTest extends PulsarTestBase {

  private static final Logger logger = LoggerFactory.getLogger(PulsarProducerTest.class);

  @Test(timeout = 20000)
  public void testProducerWithoutOptions(TestContext context) {
    String topic = UUID.randomUUID().toString();
    String sentContent = "myMessageContent-" + topic;
    AtomicBoolean done = new AtomicBoolean();

    client = PulsarClient.create(null);
    client.connect(x -> {
      context.assertTrue(x.succeeded());
      if (x.succeeded()) {
        logger.debug("Succeeded to connect");
        PulsarConnection pulsarConnection = x.result();
        pulsarConnection.createProducer(topic, p -> {
          logger.debug("Creating Producer");
          PulsarProducer pulsarProducer = p.result();
          PulsarMessage pulsarMessage = PulsarMessage.create(new JsonObject().put("test", "test"));
          pulsarProducer.send(pulsarMessage);

        });
      } else {
        logger.debug("Failed to connect");

      }
      done.set(true);
    });
    await().untilAtomic(done, is(true));

  }
}
