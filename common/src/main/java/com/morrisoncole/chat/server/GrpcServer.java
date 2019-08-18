package com.morrisoncole.chat.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(GrpcServer.class.getName());

    private final Server server;

    public GrpcServer(BindableService service, int port) {
        this.server = ServerBuilder
                .forPort(port)
                .addService(service)
                .build();
    }

    public void start() throws IOException {
        server.start();

        LOGGER.info("Server started on port: " + server.getPort());
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
