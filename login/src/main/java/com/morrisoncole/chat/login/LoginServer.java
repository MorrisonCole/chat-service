package com.morrisoncole.chat.login;

import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.LoginServiceGrpc.LoginServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginServer {
    private static final Logger LOGGER = Logger.getLogger(LoginServer.class.getName());

    private final Server server;

    public LoginServer(int port) {
        this.server = ServerBuilder
                .forPort(port)
                .addService(new LoginService())
                .build();
    }

    public void start() throws IOException {
        server.start();

        LOGGER.info("Login server started!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            LoginServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class LoginService extends LoginServiceImplBase {

        @Override
        public void login(Login.LoginRequest request, StreamObserver<Login.LoginResponse> responseObserver) {
            responseObserver.onNext(Login.LoginResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}
