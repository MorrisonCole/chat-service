package container;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class DatastoreContainer extends GenericContainer<DatastoreContainer> {

    private final String networkAlias;
    private static final int PORT = 8888;
    private final String projectName;

    public DatastoreContainer(String prefix) {
        super("google/cloud-sdk:latest");
        networkAlias = prefix + "datastore";
        projectName = prefix + "testdatastore";

        withExposedPorts(PORT);
        withNetworkAliases(networkAlias);
        withCommand("/bin/sh",
                "-c",
                String.format("gcloud beta emulators datastore start --no-legacy --project %s --host-port=0.0.0.0:%d --consistency=1",
                        projectName,
                        PORT)
        );
        waitingFor(Wait.forHttp(""));
    }

    public Datastore getDatastoreService() {
        String host = String.format("%s:%d", getContainerIpAddress(), getMappedPort(PORT));

        return DatastoreOptions.newBuilder()
                .setProjectId(projectName)
                .setHost(host)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();
    }
}
