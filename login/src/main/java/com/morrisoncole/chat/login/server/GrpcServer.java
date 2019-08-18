package com.morrisoncole.chat.login.server;

import java.io.IOException;

public interface GrpcServer {

    void start() throws IOException;

    void stop();

    void blockUntilShutdown() throws InterruptedException;
}
