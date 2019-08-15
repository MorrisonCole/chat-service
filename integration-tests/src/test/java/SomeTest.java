import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class SomeTest {

    @Container
    public GenericContainer cache = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("../cache"))
            .withFileFromPath("Dockerfile", Paths.get("../cache/Dockerfile"))
    );

    // TODO expose ports .withExposedPorts(50051)
    @Container
    public GenericContainer login = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("../login"))
            .withFileFromPath("Dockerfile", Paths.get("../login/Dockerfile"))
    );

    @Container
    public GenericContainer presence = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("../presence"))
            .withFileFromPath("Dockerfile", Paths.get("../presence/Dockerfile"))
    );

    @Test
    void someTestMethod() {
        String address = login.getContainerIpAddress();
        Integer port = login.getFirstMappedPort();
        ChatClient chatClient = new ChatClient(address, port);
        chatClient.SendMessage("Test");

        assertTrue(true);
    }
}