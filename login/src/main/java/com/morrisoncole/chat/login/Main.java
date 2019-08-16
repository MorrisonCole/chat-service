package com.morrisoncole.chat.login;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

public class Main {

    public static final String ENV_DATASTORE_PROJECT_ID = "DATASTORE_PROJECT_ID";
    public static final String ENV_DATASTORE_HOST = "DATASTORE_HOST";

    public static void main(String[] args) throws Exception {
        String projectId = System.getenv(ENV_DATASTORE_PROJECT_ID);
        String host = System.getenv(ENV_DATASTORE_HOST);

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
