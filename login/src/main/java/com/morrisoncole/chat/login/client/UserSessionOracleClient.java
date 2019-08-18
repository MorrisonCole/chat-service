package com.morrisoncole.chat.login.client;

import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.SessionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class UserSessionOracleClient {

    private final SessionServiceGrpc.SessionServiceBlockingStub sessionServiceBlockingStub;

    public UserSessionOracleClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        sessionServiceBlockingStub = SessionServiceGrpc.newBlockingStub(channel);
    }

    public Login.LoginResponse startSession(Login.LoginRequest loginRequest) {
        return sessionServiceBlockingStub.startSession(loginRequest);
    }
}
