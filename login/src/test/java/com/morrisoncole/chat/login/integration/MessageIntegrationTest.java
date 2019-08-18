package com.morrisoncole.chat.login.integration;

import client.TestLoginClient;
import client.TestMessageClient;
import container.DatastoreContainer;
import container.LoginContainer;
import container.PresenceContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
class MessageIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageIntegrationTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private static final String A_TEST_USER_ID = "a test userId";
    private static final String ANOTHER_TEST_USER_ID = "another test userId";

    private final Network network = Network.newNetwork();

    private TestLoginClient testLoginClient;

    @Container
    private DatastoreContainer loginDatastoreContainer = new DatastoreContainer("login")
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @Container
    private DatastoreContainer messageDatastoreContainer = new DatastoreContainer("message")
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

        testMessageClient.sendMessage("a random message");

        assertThat(testMessageClient.successfullySentMessage(), equalTo(true));
    }

    @Test
    void receivesHistoricalMessagesUponLogin() {
        testLoginClient.Login(A_TEST_USER_ID);

        TestMessageClient testMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(testLoginClient.getUserSessionPort()));

        ArrayList<String> expectedMessages = new ArrayList<String>() {
            {
                add("First message");
                add("Second message");
                add("Third message");
            }
        };

        for (String expectedMessage : expectedMessages) {
            testMessageClient.sendMessage(expectedMessage);
        }

        TestLoginClient anotherTestLoginClient = aTestLoginClient();
        anotherTestLoginClient.Login(ANOTHER_TEST_USER_ID);

        TestMessageClient anotherTestMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(anotherTestLoginClient.getUserSessionPort()));

        anotherTestMessageClient.getMessages();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertThat(anotherTestMessageClient.getReceivedMessages(), equalTo(expectedMessages));
        });

        assertThat(anotherTestMessageClient.getReceivedMessages(), equalTo(expectedMessages));
    }

    @Test
    @Disabled
    void receivesMessagesFromOtherUsersWithinASecond() {
        testLoginClient.Login(A_TEST_USER_ID);

        TestMessageClient testMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(testLoginClient.getUserSessionPort()));

        ArrayList<String> expectedMessages = new ArrayList<String>() {
            {
                add("hello user 2");
                add("how are you?");
            }
        };

        TestLoginClient anotherTestLoginClient = aTestLoginClient();
        anotherTestLoginClient.Login(ANOTHER_TEST_USER_ID);

        TestMessageClient anotherTestMessageClient = new TestMessageClient(
                presenceContainer.getContainerIpAddress(),
                presenceContainer.getMappedPort(anotherTestLoginClient.getUserSessionPort()));

        anotherTestMessageClient.getMessages();

        for (String expectedMessage : expectedMessages) {
            testMessageClient.sendMessage(expectedMessage);
        }

        await().atMost(1, SECONDS).untilAsserted(() -> {
            assertThat(anotherTestMessageClient.getReceivedMessages(), equalTo(expectedMessages));
        });

        assertThat(anotherTestMessageClient.getReceivedMessages(), equalTo(expectedMessages));
    }

    private TestLoginClient aTestLoginClient() {
        String address = loginContainer.getContainerIpAddress();
        Integer port = loginContainer.getFirstMappedPort();

        return new TestLoginClient(address, port);
    }
}