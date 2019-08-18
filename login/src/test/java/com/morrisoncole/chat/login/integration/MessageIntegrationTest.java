package com.morrisoncole.chat.login.integration;

import client.TestLoginClient;
import client.TestMessageClient;
import container.DatastoreContainer;
import container.LoginContainer;
import container.PresenceContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class MessageIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageIntegrationTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private static final String A_TEST_USER_ID = "a test userId";

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

    @Container
    private PresenceContainer presenceContainer = new PresenceContainer()
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @BeforeEach
    void setup() {
        testLoginClient = aTestLoginClient();
    }

    @Test
    void canSendMessage() {
        testLoginClient.Login(A_TEST_USER_ID);

        TestMessageClient testMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(testLoginClient.getUserSessionPort()));

        testMessageClient.SendMessage("a random message");

        assertTrue(testMessageClient.successfullySentMessage());
    }

    @Test
    void receivesHistoricalMessagesUponLogin() {
        testLoginClient.Login(A_TEST_USER_ID);

        TestMessageClient testMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(testLoginClient.getUserSessionPort()));

        testMessageClient.GetMessages();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertEquals("Initial message", testMessageClient.getReceivedMessage());
        });
    }

    private TestLoginClient aTestLoginClient() {
        String address = loginContainer.getContainerIpAddress();
        Integer port = loginContainer.getFirstMappedPort();

        return new TestLoginClient(address, port);
    }
}