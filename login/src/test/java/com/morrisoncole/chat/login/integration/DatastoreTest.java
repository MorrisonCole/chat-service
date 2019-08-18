package com.morrisoncole.chat.login.integration;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.morrisoncole.chat.login.integration.container.DatastoreContainer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class DatastoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatastoreTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    @Container
    private DatastoreContainer datastoreContainer = new DatastoreContainer()
            .withLogConsumer(LOG_CONSUMER);

    @Test
    void testIdAllocation() {
        Datastore datastoreService = datastoreContainer.getDatastoreService();

        KeyFactory keyFactory = datastoreService.newKeyFactory().setKind("A test kind");

        Key allocatedId = datastoreService.allocateId(keyFactory.newKey());
        assertTrue(allocatedId.getId() > 0);
    }
}
