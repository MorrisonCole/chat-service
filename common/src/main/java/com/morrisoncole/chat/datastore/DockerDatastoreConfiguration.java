package com.morrisoncole.chat.datastore;

import com.morrisoncole.chat.config.DatastoreConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class DockerDatastoreConfiguration implements DatastoreConfiguration {

    private static final Logger LOGGER = Logger.getLogger(DockerDatastoreConfiguration.class.getName());
    private static final String SECRETS_DIR = "/run/secrets/";
    private static final String DATASTORE_CONFIG_FILE_NAME_SUFFIX = "-datastore-config";
    private static final String DATASTORE_CREDENTIALS_FILE_NAME_SUFFIX = "-datastore-credentials";

    private final String prefix;
    private final HashMap<String, String> configuration = new HashMap<>();

    public DockerDatastoreConfiguration(String prefix) {
        this.prefix = prefix;

        try {
            Scanner scanner = new Scanner(new File(getDatastoreConfigFilePath()));

            while (scanner.hasNextLine()) {
                String[] pair = scanner.nextLine().split("=");
                configuration.put(pair[0], pair[1]);
            }
        } catch (FileNotFoundException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    @Nullable
    @Override
    public String getProjectId() {
        return configuration.getOrDefault("projectId", null);
    }

    @Nullable
    @Override
    public String getHost() {
        return configuration.getOrDefault("host", null);
    }

    @Override
    public boolean getUseCredentials() {
        return Boolean.parseBoolean(configuration.getOrDefault("useCredentials", "false"));
    }

    @Override
    public String getCredentialsPath() {
        return SECRETS_DIR + prefix + DATASTORE_CREDENTIALS_FILE_NAME_SUFFIX;
    }

    public String getDatastoreConfigFilePath() {
        return SECRETS_DIR + prefix + DATASTORE_CONFIG_FILE_NAME_SUFFIX;
    }
}
