package com.morrisoncole.chat.login.server;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.morrisoncole.chat.ErrorOuterClass;
import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.LoginServiceGrpc.LoginServiceImplBase;
import com.morrisoncole.chat.login.client.UserSessionOracleClient;
import com.morrisoncole.chat.login.client.UserSessionOracleClientProvider;
import com.morrisoncole.chat.schema.User;
import com.morrisoncole.chat.server.GrpcServer;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class LoginServer extends GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(LoginServer.class.getName());

    public LoginServer(Datastore datastore, int port, UserSessionOracleClientProvider userSessionOracleClientProvider) {
        super(new LoginService(datastore, userSessionOracleClientProvider), port);
    }

    private static class LoginService extends LoginServiceImplBase {

        private final Datastore datastore;
        private final UserSessionOracleClientProvider userSessionOracleClientProvider;
        private final KeyFactory keyFactory;

        LoginService(Datastore datastore, UserSessionOracleClientProvider userSessionOracleClientProvider) {
            this.datastore = datastore;
            this.userSessionOracleClientProvider = userSessionOracleClientProvider;

            keyFactory = datastore.newKeyFactory().setKind(User.KIND.toString());
        }

        @Override
        public void login(Login.LoginRequest request, StreamObserver<Login.LoginResponse> responseObserver) {
            String userId = request.getUser().getUserId();

            if (userExists(userId)) {
                respondWithError(responseObserver);
                return;
            }

            UserSessionOracleClient userSessionOracleClient = userSessionOracleClientProvider
                    .getInstance();

            if (userSessionOracleClient == null) {
                respondWithError(responseObserver);
                return;
            }

            Login.LoginResponse loginResponse = userSessionOracleClient
                    .startSession(request);

            if (loginResponse.hasError()) {
                respondWithError(responseObserver);
                return;
            }

            datastore.add(createUserWithId(userId));
            respondWithSuccess(responseObserver, loginResponse);
        }

        private boolean userExists(String userId) {
            EntityQuery query = Query.newEntityQueryBuilder()
                    .setKind(User.KIND.toString())
                    .setFilter(PropertyFilter.eq(User.ID.toString(), userId))
                    .build();
            return datastore.run(query).hasNext();
        }

        private void respondWithError(StreamObserver<Login.LoginResponse> responseObserver) {
            responseObserver.onNext(Login.LoginResponse.newBuilder()
                    .setError(ErrorOuterClass.Error.newBuilder().build())
                    .build());
            responseObserver.onCompleted();
        }

        private FullEntity<IncompleteKey> createUserWithId(String userId) {
            IncompleteKey key = keyFactory.newKey();
            return Entity.newBuilder(key)
                    .set(User.ID.toString(), userId)
                    .build();
        }

        private void respondWithSuccess(StreamObserver<Login.LoginResponse> responseObserver, Login.LoginResponse response) {
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
