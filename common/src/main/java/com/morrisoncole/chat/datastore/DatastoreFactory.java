package com.morrisoncole.chat.datastore;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.morrisoncole.chat.config.DatastoreConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import static com.google.auth.oauth2.UserCredentials.fromStream;

public class DatastoreFactory {

    private static final Logger LOGGER = Logger.getLogger(DatastoreFactory.class.getName());

    private final DatastoreConfiguration configuration;

    public DatastoreFactory(DatastoreConfiguration configuration) {
        this.configuration = configuration;
    }

    public Datastore newDatastore() {
        DatastoreOptions.Builder datastoreBuilder = DatastoreOptions.newBuilder();

        if (configuration.getProjectId() != null) {
            datastoreBuilder.setProjectId(configuration.getProjectId());
        }
        if (configuration.getUseCredentials()) {
            try {
                datastoreBuilder.setCredentials(fromStream(new FileInputStream(configuration.getCredentialsPath())));
            } catch (IOException e) {
                LOGGER.warning("Could not load datastore credentials: " + e.getMessage());
            }
        }
        if (configuration.getHost() != null) {
            datastoreBuilder
                    .setHost(configuration.getHost())
                    .setCredentials(NoCredentials.getInstance());
        }

        return datastoreBuilder
                .build()
                .getService();
    }
}
