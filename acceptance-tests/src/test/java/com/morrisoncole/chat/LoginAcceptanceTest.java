package com.morrisoncole.chat;

import client.TestLoginClient;
import container.DatastoreContainer;
import container.LoginContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class LoginAcceptanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAcceptanceTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private final Network network = Network.newNetwork();

    @Container
    private DatastoreContainer datastoreContainer = new DatastoreContainer("login")
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @Container
    private LoginContainer loginContainer = new LoginContainer()
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @Test
    void canHandleAtLeast1000RegisteredUsers() {
        int targetLoggedInUsers = 1000;

        int actualLoggedInUsers = 0;
        for (int i = 0; i < targetLoggedInUsers; i++) {
            TestLoginClient client = aTestLoginClient();
            client.Login(UUID.randomUUID().toString());
            if (client.isLoggedIn()) {
                actualLoggedInUsers++;
            }
        }

        assertEquals(targetLoggedInUsers, actualLoggedInUsers);
    }

    private TestLoginClient aTestLoginClient() {
        String address = loginContainer.getContainerIpAddress();
        Integer port = loginContainer.getFirstMappedPort();

        return new TestLoginClient(address, port);
    }
}