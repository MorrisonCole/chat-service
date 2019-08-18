package com.morrisoncole.chat.login.server.session;

import com.google.protobuf.Empty;
import com.morrisoncole.chat.MessageOuterClass;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceImplBase;
import com.morrisoncole.chat.login.server.GrpcServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class UserSessionServer implements GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(UserSessionServer.class.getName());

    private final String userId;
    private final Server server;

    public UserSessionServer(String userId, int port) {
        this.userId = userId;
        this.server = ServerBuilder
                .forPort(port)
                .addService(new UserSessionService())
                .build();
    }

    @Override
    public void start() throws IOException {
        server.start();

        LOGGER.info("Login server started!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            UserSessionServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class UserSessionService extends MessageServiceImplBase {

        @Override
        public void sendMessage(MessageOuterClass.Message request, StreamObserver<Empty> responseObserver) {
            LOGGER.warning("Sent message: " + request.getMessage());
        }
    }
}
