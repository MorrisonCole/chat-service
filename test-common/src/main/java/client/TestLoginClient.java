package client;

import com.morrisoncole.chat.Login;
import com.morrisoncole.chat.Login.LoginRequest;
import com.morrisoncole.chat.LoginServiceGrpc;
import com.morrisoncole.chat.LoginServiceGrpc.LoginServiceBlockingStub;
import com.morrisoncole.chat.UserOuterClass.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class TestLoginClient {

    private final LoginServiceBlockingStub loginServiceBlockingStub;

    private boolean loggedIn;
    private int userSessionPort;

    public TestLoginClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        loginServiceBlockingStub = LoginServiceGrpc.newBlockingStub(channel);
    }

    public void Login(String userId) {
        User user = User.newBuilder()
                .setUserId(userId)
                .build();
        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUser(user)
                .build();

        Login.LoginResponse response = loginServiceBlockingStub.login(loginRequest);

        bind(response);
    }

    private void bind(Login.LoginResponse response) {
        loggedIn = !response.hasError();
        userSessionPort = response.getPort();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public int getUserSessionPort() {
        return userSessionPort;
    }
}