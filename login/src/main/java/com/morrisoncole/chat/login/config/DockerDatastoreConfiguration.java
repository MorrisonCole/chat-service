package com.morrisoncole.chat.login.config;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class DockerDatastoreConfiguration implements DatastoreConfiguration {

    public static final String SECRETS_DIR = "/run/secrets/";
    public static final String DATASTORE_CONFIG_FILE_NAME = "login-datastore-config";

    private static final String DATASTORE_CREDENTIALS_FILE_NAME = "login-datastore-credentials";
    private static final Logger LOGGER = Logger.getLogger(DockerDatastoreConfiguration.class.getName());

    private final HashMap<String, String> configuration = new HashMap<>();

    public DockerDatastoreConfiguration() {
        try {
            Scanner scanner = new Scanner(new File(SECRETS_DIR + DATASTORE_CONFIG_FILE_NAME));

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
        return SECRETS_DIR + DATASTORE_CREDENTIALS_FILE_NAME;
    }
}
