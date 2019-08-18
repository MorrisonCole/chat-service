package container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

public class PresenceContainer extends GenericContainer<PresenceContainer> {

    public PresenceContainer() {
        super(new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("../presence"))
                .withFileFromPath("Dockerfile", Paths.get("../presence/Dockerfile")));

        withExposedPorts(50052, 51000);
        withNetworkAliases("presence");
        waitingFor(Wait.forLogMessage(".*started.*", 1));
    }
}
