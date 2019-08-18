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

    private TestLoginClient testLoginClient;

    @Container
    private DatastoreContainer datastoreContainer = new DatastoreContainer("login")
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @Container
    private LoginContainer loginContainer = new LoginContainer()
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @BeforeEach
    void setup() {
        String address = loginContainer.getContainerIpAddress();
        Integer port = loginContainer.getFirstMappedPort();

        testLoginClient = new TestLoginClient(address, port);
    }

    @Test
    void canHandleAtLeast1000RegisteredUsers() {
        int targetLoggedInUsers = 1000;

        int actualLoggedInUsers = 0;
        for (int i = 0; i < targetLoggedInUsers; i++) {
            boolean loggedIn = testLoginClient.Login(UUID.randomUUID().toString());
            if (loggedIn) {
                actualLoggedInUsers++;
            }
        }

        assertEquals(targetLoggedInUsers, actualLoggedInUsers);
    }
}