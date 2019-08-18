package com.morrisoncole.chat.presence;

import com.morrisoncole.chat.datastore.DatastoreFactory;
import com.morrisoncole.chat.datastore.DockerDatastoreConfiguration;
import com.morrisoncole.chat.presence.server.session.UserSessionOracleServer;

public class Main {

    private static final DatastoreFactory datastoreFactory = new DatastoreFactory(new DockerDatastoreConfiguration("message"));

    public static void main(String[] args) throws Exception {
        UserSessionOracleServer userSessionOracleServer = new UserSessionOracleServer(datastoreFactory, 50052);
        userSessionOracleServer.start();
        userSessionOracleServer.blockUntilShutdown();
    }
}
