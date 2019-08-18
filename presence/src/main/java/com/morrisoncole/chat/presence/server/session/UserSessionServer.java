package com.morrisoncole.chat.presence.server.session;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.protobuf.Empty;
import com.morrisoncole.chat.MessageOuterClass;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceImplBase;
import com.morrisoncole.chat.UserOuterClass;
import com.morrisoncole.chat.schema.Message;
import com.morrisoncole.chat.server.GrpcServer;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class UserSessionServer extends GrpcServer {

    private static final Logger LOGGER = Logger.getLogger(UserSessionServer.class.getName());

    UserSessionServer(Datastore datastore, String userId, int port) {
        super(new UserSessionService(datastore, userId), port);
    }

    private static class UserSessionService extends MessageServiceImplBase {

        private final Datastore datastore;
        private final String userId;
        private final KeyFactory keyFactory;

        UserSessionService(Datastore datastore, String userId) {
            this.datastore = datastore;
            this.userId = userId;

            keyFactory = datastore.newKeyFactory().setKind(Message.KIND.toString());
        }

        @Override
        public void sendMessage(MessageOuterClass.Message request, StreamObserver<Empty> responseObserver) {
            datastore.put(createMessage(request.getMessage()));

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void getMessages(Empty request, StreamObserver<MessageOuterClass.Message> responseObserver) {
            ArrayList<MessageOuterClass.Message> last100Messages = getLast100Messages();

            for (MessageOuterClass.Message message : last100Messages) {
                LOGGER.severe("responding with message: " + message.getMessage());
                responseObserver.onNext(message);
            }

            responseObserver.onCompleted();
        }

        private FullEntity<IncompleteKey> createMessage(String content) {
            IncompleteKey key = keyFactory.newKey();
            return Entity.newBuilder(key)
                    .set(Message.USER_ID.toString(), userId)
                    .set(Message.TIMESTAMP.toString(), Timestamp.now())
                    .set(Message.CONTENT.toString(), content)
                    .build();
        }

        private ArrayList<MessageOuterClass.Message> getLast100Messages() {
            EntityQuery query = Query.newEntityQueryBuilder()
                    .setKind(Message.KIND.toString())
                    .setOrderBy(OrderBy.asc(Message.TIMESTAMP.toString()))
                    .setLimit(100)
                    .build();

            ArrayList<MessageOuterClass.Message> messages = new ArrayList<>();
            QueryResults<Entity> results = datastore.run(query);
            while (results.hasNext()) {
                Entity result = results.next();

                String userId = result.getString(Message.USER_ID.toString());
                String content = result.getString(Message.CONTENT.toString());
                Timestamp timestamp = result.getTimestamp(Message.TIMESTAMP.toString());

                MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                        .setUser(UserOuterClass.User.newBuilder().setUserId(userId).build())
                        .setMessage(content)
                        .setTimestamp(timestamp.toProto())
                        .build();

                messages.add(message);
            }

            return messages;
        }
    }
}
