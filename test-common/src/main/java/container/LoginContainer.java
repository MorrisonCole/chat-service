package container;

import com.morrisoncole.chat.datastore.DockerDatastoreConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

public class LoginContainer extends GenericContainer<LoginContainer> {

    public LoginContainer() {
        super(new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("../login"))
                .withFileFromPath("Dockerfile", Paths.get("../login/Dockerfile")));

        withCopyFileToContainer(
                MountableFile.forClasspathResource("TEST_LOGIN_DATASTORE_CONFIG"),
                new DockerDatastoreConfiguration("login").getDatastoreConfigFilePath());
        withExposedPorts(50051);
        waitingFor(Wait.forLogMessage(".*started.*", 1));
    }
}
