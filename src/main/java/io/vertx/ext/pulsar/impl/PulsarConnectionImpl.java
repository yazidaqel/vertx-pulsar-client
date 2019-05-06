package io.vertx.ext.pulsar.impl;

import io.vertx.core.*;
import io.vertx.ext.pulsar.*;
import org.apache.pulsar.client.api.PulsarClient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PulsarConnectionImpl implements PulsarConnection {

  private final AtomicBoolean closed = new AtomicBoolean();

  private final AtomicReference<PulsarClient> connection = new AtomicReference<>();

  private final List<PulsarConsumer> consumers = new CopyOnWriteArrayList<>();
  private final List<PulsarProducer> producers = new CopyOnWriteArrayList<>();
  private PulsarClientOptions options;
  private Context context;
  private Handler<Throwable> exceptionHandler;

  private String serviceUrl;

  PulsarConnectionImpl(Context context, PulsarClientImpl client, PulsarClientOptions options,
                       Handler<AsyncResult<PulsarConnection>> connectionHandler) {
    this.options = options;
    this.context = context;
    this.serviceUrl = "pulsar://" + this.options.getHost() + ":" + this.options.getPort();

    runOnContext(x -> connect(client,
      Objects.requireNonNull(connectionHandler, "connection handler cannot be `null`"))
    );
  }

  private void connect(PulsarClientImpl client, Handler<AsyncResult<PulsarConnection>> connectionHandler) {
    try {

      if (connection.get() != null && !closed.get()) {
        connectionHandler.handle(Future.failedFuture("Already connected"));
        return;
      }
      PulsarClient pulsarClient = PulsarClient.builder()
        .serviceUrl(this.serviceUrl)
        .build();

      connection.set(pulsarClient);
      closed.set(false);
      client.register(this);
    } catch (Exception ex) {
      connectionHandler.handle(Future.failedFuture(ex));
    } finally {
      closed.set(true);
    }
  }


  @Override
  public PulsarConnection exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public PulsarConnection close(Handler<AsyncResult<Void>> done) {

    context.runOnContext(ignored -> {
      PulsarClient pulsarClient = connection.get();
      if (pulsarClient == null || closed.get()) {
        done.handle(Future.failedFuture("No opening connection to close"));
        return;
      }
      try {
        CompletableFuture future = pulsarClient.closeAsync();
        future.whenComplete((r, e) -> {
          if (e == null) {
            done.handle(Future.succeededFuture());
          } else {
            done.handle(Future.failedFuture((Exception) e));
          }
        });
      } catch (Exception ex) {
        done.handle(Future.failedFuture(ex));
      } finally {
        closed.set(true);
      }
    });
    return this;
  }

  @Override
  public PulsarConnection createConsumer(String topic, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    return createConsumer(topic, null, completionHandler);
  }

  @Override
  public PulsarConnection createConsumer(String topic, PulsarConsumerOptions options, Handler<AsyncResult<PulsarConsumer>> completionHandler) {
    PulsarConsumerOptions pulsarConsumerOptions = options == null ? new PulsarConsumerOptions() : options;
    runWithTrampoline(x -> {
      new PulsarConsumerImpl(topic, this, options, null, completionHandler);
    });
    return this;
  }

  @Override
  public PulsarConnection createProducer(String topic, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return createProducer(topic, null, completionHandler);
  }

  @Override
  public PulsarConnection createProducer(String topic, PulsarProducerOptions options, Handler<AsyncResult<PulsarProducer>> completionHandler) {
    return this;
  }

  void runOnContext(Handler<Void> action) {
    context.runOnContext(action);
  }

  void runWithTrampoline(Handler<Void> action) {
    if (Vertx.currentContext() == context) {
      action.handle(null);
    } else {
      runOnContext(action);
    }
  }

  protected PulsarClient connection() {
    return connection.get();
  }

  protected boolean isClosed(){
    return closed.get();
  }

  protected void register(PulsarConsumer consumer){
    consumers.add(consumer);
  }
  protected void register(PulsarProducer producer){
    producers.add(producer);
  }

  protected void unregister(PulsarConsumer consumer){
    consumers.remove(consumer);
  }
  protected void unregister(PulsarProducer producer){
    producers.remove(producer);
  }
}
