package com.morrisoncole.chat.login;

public class Main {

    public static void main(String[] args) throws Exception {
        LoginServer loginServer = new LoginServer(50051);
        loginServer.start();
        loginServer.blockUntilShutdown();
    }
}
