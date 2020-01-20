**This is not an official Vert.x extension**

**Eclipse Vert.x Pulsar Client**

This project is intended to add a Vert.x extension to
enable reactive communication with https://pulsar.apache.org

Currently this extension is still under development, so feel free to fork and contribute.

**Documentation**

1- Create a pulsar connection using PulsarClient object

client = PulsarClient.create(vertx, null);

client.connect(handler -> {

    if (handler.succeeded()) {

        LOGGER.info("Connection to the Pulsar succeeded");

    } else {

        LOGGER.info("Connection to the Pulsar failed");

    }

});

**Requirements**

Docker (used with Test Container)

Java 8

Maven

**Instructions**

mvn clean install




