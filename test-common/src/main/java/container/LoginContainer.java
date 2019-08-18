package container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static com.morrisoncole.chat.login.config.DockerDatastoreConfiguration.DATASTORE_CONFIG_FILE_NAME;
import static com.morrisoncole.chat.login.config.DockerDatastoreConfiguration.SECRETS_DIR;

public class LoginContainer extends GenericContainer<LoginContainer> {

    public LoginContainer() {
        super(new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("../login"))
                .withFileFromPath("Dockerfile", Paths.get("../login/Dockerfile")));

        withCopyFileToContainer(
                MountableFile.forClasspathResource("TEST_LOGIN_DATASTORE_CONFIG"),
                SECRETS_DIR + DATASTORE_CONFIG_FILE_NAME);
        withExposedPorts(50051);
        waitingFor(Wait.forLogMessage(".*started.*", 1));
    }
}
