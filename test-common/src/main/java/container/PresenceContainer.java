package container;

import com.morrisoncole.chat.datastore.DockerDatastoreConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

public class PresenceContainer extends GenericContainer<PresenceContainer> {

    public PresenceContainer() {
        super(new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("../presence"))
                .withFileFromPath("Dockerfile", Paths.get("../presence/Dockerfile")));

        withCopyFileToContainer(
                MountableFile.forClasspathResource("TEST_MESSAGE_DATASTORE_CONFIG"),
                new DockerDatastoreConfiguration("message").getDatastoreConfigFilePath());
        withExposedPorts(50052, 51000, 51001);
        withNetworkAliases("presence");
        waitingFor(Wait.forLogMessage(".*started.*", 1));
    }
}
