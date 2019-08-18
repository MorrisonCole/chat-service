package com.morrisoncole.chat.presence.server.session;

import com.google.protobuf.Empty;
import com.morrisoncole.chat.MessageOuterClass;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceImplBase;
import com.morrisoncole.chat.server.GrpcServer;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

class UserSessionServer extends GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(UserSessionServer.class.getName());

    private final String userId;

    UserSessionServer(String userId, int port) {
        super(new UserSessionService(), port);
        this.userId = userId;
    }

    private static class UserSessionService extends MessageServiceImplBase {

        @Override
        public void sendMessage(MessageOuterClass.Message request, StreamObserver<Empty> responseObserver) {
            LOGGER.warning("Sent message: " + request.getMessage());
        }
    }
}
