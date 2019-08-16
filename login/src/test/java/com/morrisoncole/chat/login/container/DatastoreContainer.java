package com.morrisoncole.chat.login.container;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class DatastoreContainer extends GenericContainer<DatastoreContainer> {

    private String projectName = "testing";
    private int emulatorPort = 8888;

    public DatastoreContainer() {
        super("google/cloud-sdk:latest");

        withExposedPorts(emulatorPort);
        withNetworkAliases("datastore");
        withCommand("/bin/sh",
                "-c",
                String.format("gcloud beta emulators datastore start --no-legacy --project %s --host-port=0.0.0.0:%d --consistency=1",
                        projectName,
                        emulatorPort)
        );
        waitingFor(Wait.forHttp(""));
    }

    public Datastore getDatastoreService() {
        String host = String.format("%s:%d", getContainerIpAddress(), getMappedPort(emulatorPort));

        return DatastoreOptions.newBuilder()
                .setProjectId(projectName)
                .setHost(host)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();
    }
}
