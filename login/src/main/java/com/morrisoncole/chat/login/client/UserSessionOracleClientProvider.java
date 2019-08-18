package com.morrisoncole.chat.login.client;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class UserSessionOracleClientProvider {

    private static final Logger LOGGER = Logger.getLogger(UserSessionOracleClientProvider.class.getName());

    private UserSessionOracleClient instance;
    private final String host;
    private final int port;

    public UserSessionOracleClientProvider(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Nullable
    public UserSessionOracleClient getInstance() {
        if (instance == null) {
            try {
                instance = new UserSessionOracleClient(InetAddress.getByName(host).getHostAddress(), port);
            } catch (UnknownHostException e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return instance;
    }
}
