package com.morrisoncole.chat.login;

import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.Login.LoginRequest;
import com.morrisoncole.chat.LoginServiceGrpc;
import com.morrisoncole.chat.LoginServiceGrpc.LoginServiceBlockingStub;
import com.morrisoncole.chat.LoginServiceGrpc.LoginServiceStub;
import com.morrisoncole.chat.UserOuterClass.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

class TestLoginClient {

    private final LoginServiceStub loginServiceStub;
    private final LoginServiceBlockingStub loginServiceBlockingStub;

    TestLoginClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        loginServiceBlockingStub = LoginServiceGrpc.newBlockingStub(channel);
        loginServiceStub = LoginServiceGrpc.newStub(channel);
    }

    boolean Login(String userId) {
        User user = User.newBuilder()
                .setUserId(userId)
                .build();
        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUser(user)
                .build();

        Login.LoginResponse response = loginServiceBlockingStub.login(loginRequest);
        return !response.hasError();
    }
}