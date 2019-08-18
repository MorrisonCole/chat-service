package com.morrisoncole.chat.login.integration;

import com.morrisoncole.chat.login.integration.client.TestLoginClient;
import com.morrisoncole.chat.login.integration.container.DatastoreContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static com.morrisoncole.chat.login.config.DockerDatastoreConfiguration.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class LoginIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginIntegrationTest.class);
    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    private static final String A_TEST_USER_ID = "a test userId";
    private static final String ANOTHER_TEST_USER_ID = "another test userId";

    private final Network network = Network.newNetwork();

    private TestLoginClient testLoginClient;

    @Container
    private DatastoreContainer datastoreContainer = new DatastoreContainer()
            .withNetwork(network)
            .withLogConsumer(LOG_CONSUMER);

    @Container
    private GenericContainer loginContainer = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("./"))
            .withFileFromPath("Dockerfile", Paths.get("./Dockerfile")))
            .withCopyFileToContainer(MountableFile.forClasspathResource("TESTING_DATASTORE_CONFIG"), SECRETS_DIR + DATASTORE_CONFIG_FILE_NAME)
            .withLogConsumer(LOG_CONSUMER)
            .withExposedPorts(50051)
            .waitingFor(Wait.forLogMessage(".*started.*", 1))
            .withNetwork(network);

    @BeforeEach
    void setup() {
        String address = loginContainer.getContainerIpAddress();
        Integer port = loginContainer.getFirstMappedPort();

        testLoginClient = new TestLoginClient(address, port);
    }

    @Test
    void canLogin() {
        boolean loggedIn = testLoginClient.Login(A_TEST_USER_ID);

        assertTrue(loggedIn);
    }

    @Test
    void attemptToLoginWithUsedUserIdFails() {
        testLoginClient.Login(A_TEST_USER_ID);
        boolean secondUserLoggedIn = testLoginClient.Login(A_TEST_USER_ID);

        assertFalse(secondUserLoggedIn);
    }

    @Test
    @Disabled
    void attemptToLoginWithUnusedUserIdSucceeds() {
        testLoginClient.Login(A_TEST_USER_ID);
        boolean secondUserLoggedIn = testLoginClient.Login(ANOTHER_TEST_USER_ID);

        assertTrue(secondUserLoggedIn);
    }
}