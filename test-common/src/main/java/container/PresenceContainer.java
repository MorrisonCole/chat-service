package container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static com.morrisoncole.chat.login.config.DockerDatastoreConfiguration.DATASTORE_CONFIG_FILE_NAME;
import static com.morrisoncole.chat.login.config.DockerDatastoreConfiguration.SECRETS_DIR;

public class PresenceContainer extends GenericContainer<PresenceContainer> {

    public PresenceContainer() {
        super(new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("../presence"))
                .withFileFromPath("Dockerfile", Paths.get("../presence/Dockerfile")));

        withExposedPorts(50052);
        withNetworkAliases("presence");
        waitingFor(Wait.forLogMessage(".*started.*", 1));
    }
}
