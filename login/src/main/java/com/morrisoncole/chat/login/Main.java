package com.morrisoncole.chat.login;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.morrisoncole.chat.login.config.DatastoreConfiguration;
import com.morrisoncole.chat.login.config.DockerDatastoreConfiguration;

import java.io.FileInputStream;
import java.util.logging.Logger;

import static com.google.auth.oauth2.ServiceAccountCredentials.fromStream;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final DatastoreConfiguration configuration = new DockerDatastoreConfiguration();

    public static void main(String[] args) throws Exception {
        DatastoreOptions.Builder datastoreBuilder = DatastoreOptions.newBuilder();

        if (configuration.getProjectId() != null) {
            datastoreBuilder.setProjectId(configuration.getProjectId());
        }
        if (configuration.getUseCredentials()) {
            datastoreBuilder.setCredentials(fromStream(new FileInputStream(configuration.getCredentialsPath())));
        }
        if (configuration.getHost() != null) {
            datastoreBuilder
                    .setHost(configuration.getHost())
                    .setCredentials(NoCredentials.getInstance());
        }

        Datastore datastoreService = datastoreBuilder
                .build()
                .getService();

        LoginServer loginServer = new LoginServer(datastoreService, 50051);
        loginServer.start();
        loginServer.blockUntilShutdown();
    }
}
