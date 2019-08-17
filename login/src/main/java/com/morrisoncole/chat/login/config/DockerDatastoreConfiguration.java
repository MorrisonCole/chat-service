package com.morrisoncole.chat.login.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

public class DockerDatastoreConfiguration implements DatastoreConfiguration {

    public static final String SECRETS_DIR = "/run/secrets/";
    public static final String PROJECT_ID_FILE_NAME = "PROJECT_ID";
    public static final String HOST_FILE_NAME = "HOST";

    private static final Logger LOGGER = Logger.getLogger(DockerDatastoreConfiguration.class.getName());

    @Override
    public String getProjectId() {
        return readFromFile(PROJECT_ID_FILE_NAME);
    }

    @Override
    public String getHost() {
        return readFromFile(HOST_FILE_NAME);
    }

    private String readFromFile(String fileName) {
        String result = "";
        try {
            Scanner scanner = new Scanner(new File(SECRETS_DIR + fileName));
            result = scanner.nextLine();
        } catch (FileNotFoundException e) {
            LOGGER.severe(e.getMessage());
        }
        return result;
    }
}
