package com.morrisoncole.chat.login;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

public class Main {

    public static void main(String[] args) throws Exception {
        Datastore datastore = DatastoreOptions.newBuilder()
                .setProjectId("testing") // TODO pass this config in
                .setHost("http://datastore:8888") // TODO pass this config in
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();

        LoginServer loginServer = new LoginServer(datastore, 50051);
        loginServer.start();
        loginServer.blockUntilShutdown();
    }
}
