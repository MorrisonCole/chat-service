package com.morrisoncole.chat.presence;

import com.morrisoncole.chat.presence.server.session.UserSessionOracleServer;

public class Main {

    public static void main(String[] args) throws Exception {
        UserSessionOracleServer userSessionOracleServer = new UserSessionOracleServer(50052);
        userSessionOracleServer.start();
        userSessionOracleServer.blockUntilShutdown();
    }
}
