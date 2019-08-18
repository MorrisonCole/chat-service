package com.morrisoncole.chat.presence.server.session;

import com.google.cloud.datastore.Datastore;
import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.SessionServiceGrpc.SessionServiceImplBase;
import com.morrisoncole.chat.datastore.DatastoreFactory;
import com.morrisoncole.chat.server.GrpcServer;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class UserSessionOracleServer extends GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(UserSessionOracleServer.class.getName());

    public UserSessionOracleServer(DatastoreFactory datastoreFactory, int port) {
        super(new UserSessionOracleService(datastoreFactory), port);
    }

    private static class UserSessionOracleService extends SessionServiceImplBase {

        private final DatastoreFactory datastoreFactory;

        private int lastUsedPort = 51000;

        UserSessionOracleService(DatastoreFactory datastoreFactory) {
            this.datastoreFactory = datastoreFactory;
        }

        @Override
        public void startSession(Login.LoginRequest request, StreamObserver<Login.LoginResponse> responseObserver) {
            int port = getFreePort();
            new Thread(() -> {
                try {
                    UserSessionServer userSessionServer = new UserSessionServer(datastoreFactory.newDatastore(), request.getUser().getUserId(), port);
                    userSessionServer.start();
                    userSessionServer.blockUntilShutdown();
                } catch (IOException | InterruptedException e) {
                    LOGGER.severe("User session failed: " + e.getMessage());
                }
            }).start();

            responseObserver.onNext(Login.LoginResponse.newBuilder().setPort(port).build());
            responseObserver.onCompleted();
        }

        private int getFreePort() {
            return lastUsedPort++; // TODO free up ports, this is obviously not going to last for long!
        }
    }
}
