package com.morrisoncole.chat.login;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class LoginIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginIntegrationTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private static final String A_TEST_USER_ID = "a test userId";

    private TestLoginClient testLoginClient;

    @Container
    private GenericContainer login = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("./"))
            .withFileFromPath("Dockerfile", Paths.get("./Dockerfile")))
            .withLogConsumer(LOG_CONSUMER)
            .withExposedPorts(50051)
            .waitingFor(Wait.forLogMessage(".*started.*", 1));

    @BeforeEach
    void setup() {
        String address = login.getContainerIpAddress();
        Integer port = login.getFirstMappedPort();

        testLoginClient = new TestLoginClient(address, port);
    }

    @Test
    void canLogin() {
        boolean loggedIn = testLoginClient.Login(A_TEST_USER_ID);

        assertTrue(loggedIn);
    }

    @Test
    @Disabled
    void attemptToLoginWithTakenUserIdFails() {
        testLoginClient.Login(A_TEST_USER_ID);
        boolean secondUserLoggedIn = testLoginClient.Login(A_TEST_USER_ID);

        assertFalse(secondUserLoggedIn);
    }
}