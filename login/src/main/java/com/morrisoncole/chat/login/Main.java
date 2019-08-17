package com.morrisoncole.chat.login;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.morrisoncole.chat.login.config.DatastoreConfiguration;
import com.morrisoncole.chat.login.config.DockerDatastoreConfiguration;

public class Main {

    private static final DatastoreConfiguration configuration = new DockerDatastoreConfiguration();

    public static void main(String[] args) throws Exception {
        String projectId = configuration.getProjectId();
        String host = configuration.getHost();

        Datastore datastore = DatastoreOptions.newBuilder()
                .setProjectId(projectId)
                .setHost(host)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();

        LoginServer loginServer = new LoginServer(datastore, 50051);
        loginServer.start();
        loginServer.blockUntilShutdown();
    }
}
