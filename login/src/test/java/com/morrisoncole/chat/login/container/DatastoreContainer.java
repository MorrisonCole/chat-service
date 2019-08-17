package com.morrisoncole.chat.login.container;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class DatastoreContainer extends GenericContainer<DatastoreContainer> {

    private static final String NETWORK_ALIAS = "datastore";
    private static final int PORT = 8888;
    private static final String PROJECT_NAME = "testing";

    public DatastoreContainer() {
        super("google/cloud-sdk:latest");

        withExposedPorts(PORT);
        withNetworkAliases(NETWORK_ALIAS);
        withCommand("/bin/sh",
                "-c",
                String.format("gcloud beta emulators datastore start --no-legacy --project %s --host-port=0.0.0.0:%d --consistency=1",
                        PROJECT_NAME,
                        PORT)
        );
        waitingFor(Wait.forHttp(""));
    }

    public Datastore getDatastoreService() {
        String host = String.format("%s:%d", getContainerIpAddress(), getMappedPort(PORT));

        return DatastoreOptions.newBuilder()
                .setProjectId(PROJECT_NAME)
                .setHost(host)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();
    }
}
