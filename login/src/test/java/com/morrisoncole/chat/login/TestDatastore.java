package com.morrisoncole.chat.login;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class DatastoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatastoreTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private String projectName = "testing";
    private int emulatorPort = 8888;

    @Container
    private GenericContainer datastoreContainer =
            new GenericContainer("google/cloud-sdk:latest")
                    .withExposedPorts(emulatorPort)
                    .withCommand("/bin/sh",
                            "-c",
                            String.format("gcloud beta emulators datastore start --no-legacy --project %s --host-port=0.0.0.0:%d --consistency=1",
                                    projectName,
                                    emulatorPort)
                    )
                    .withLogConsumer(LOG_CONSUMER)
                    .waitingFor(Wait.forHttp(""));

    @Test
    void testIdAllocation() {
        String host = String.format("%s:%d",
                datastoreContainer.getContainerIpAddress(),
                datastoreContainer.getMappedPort(emulatorPort));

        Datastore datastoreService = DatastoreOptions.newBuilder()
                .setProjectId(projectName)
                .setHost(host)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();

        KeyFactory keyFactory = datastoreService.newKeyFactory().setKind("A test kind");

        Key allocatedId = datastoreService.allocateId(keyFactory.newKey());
        assertTrue(allocatedId.getId() > 0);
    }
}
