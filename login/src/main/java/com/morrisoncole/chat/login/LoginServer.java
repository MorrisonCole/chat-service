package com.morrisoncole.chat.login;

import com.google.cloud.datastore.*;
import com.morrisoncole.chat.ErrorOuterClass;
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

    public LoginServer(Datastore datastore, int port) {
        this.server = ServerBuilder
                .forPort(port)
                .addService(new LoginService(datastore))
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

        private final Datastore datastore;
        private final KeyFactory keyFactory;

        LoginService(Datastore datastore) {
            this.datastore = datastore;
            keyFactory = datastore.newKeyFactory().setKind("User");
        }

        @Override
        public void login(Login.LoginRequest request, StreamObserver<Login.LoginResponse> responseObserver) {
            EntityQuery query = Query.newEntityQueryBuilder().setKind("User").build();
            if (datastore.run(query).hasNext()) {
                responseObserver.onNext(Login.LoginResponse.newBuilder()
                        .setError(ErrorOuterClass.Error.newBuilder().build())
                        .build());
                responseObserver.onCompleted();
                return;
            }

            IncompleteKey key = keyFactory.newKey();
            FullEntity<IncompleteKey> entity = Entity.newBuilder(key)
                    .set("id", request.getUser().getUserId())
                    .build();
            datastore.add(entity);

            responseObserver.onNext(Login.LoginResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}
