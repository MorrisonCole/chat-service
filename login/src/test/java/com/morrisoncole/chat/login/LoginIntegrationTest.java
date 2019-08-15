package com.morrisoncole.chat.login;

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

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class LoginIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginIntegrationTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    @Container
    public GenericContainer login = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("./"))
            .withFileFromPath("Dockerfile", Paths.get("./Dockerfile")))
            .withLogConsumer(LOG_CONSUMER)
            .withExposedPorts(50051)
            .waitingFor(Wait.forLogMessage(".*started.*", 1));

    @Test
    void canLogin() {
        String address = login.getContainerIpAddress();
        Integer port = login.getFirstMappedPort();

        TestLoginClient testLoginClient = new TestLoginClient(address, port);
        testLoginClient.Login("a test userId");

        assertTrue(true);
    }
}