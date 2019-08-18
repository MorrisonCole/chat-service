package com.morrisoncole.chat.login;

import com.morrisoncole.chat.datastore.DatastoreFactory;
import com.morrisoncole.chat.datastore.DockerDatastoreConfiguration;
import com.morrisoncole.chat.login.client.UserSessionOracleClientProvider;
import com.morrisoncole.chat.login.server.LoginServer;

public class Main {

    private static final DatastoreFactory datastoreFactory = new DatastoreFactory(new DockerDatastoreConfiguration("login"));

    public static void main(String[] args) throws Exception {
        UserSessionOracleClientProvider userSessionOracleClientProvider = new UserSessionOracleClientProvider(
                "presence",
                50052);
        LoginServer loginServer = new LoginServer(datastoreFactory.newDatastore(), 50051, userSessionOracleClientProvider);
        loginServer.start();
        loginServer.blockUntilShutdown();
    }
}
