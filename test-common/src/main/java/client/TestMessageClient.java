package client;

import com.google.protobuf.Empty;
import com.morrisoncole.chat.MessageOuterClass.Message;
import com.morrisoncole.chat.MessageServiceGrpc;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceBlockingStub;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.logging.Logger;

public class TestMessageClient {

    private static final Logger LOGGER = Logger.getLogger(TestMessageClient.class.getName());

    private final MessageServiceStub messageServiceStub;
    private final MessageServiceBlockingStub messageServiceBlockingStub;

    private boolean sentMessage = false;
    private String receivedMessage;

    public TestMessageClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        messageServiceStub = MessageServiceGrpc.newStub(channel);
        messageServiceBlockingStub = MessageServiceGrpc.newBlockingStub(channel);
    }

    public void SendMessage(String content) {
        Message message = Message.newBuilder()
                .setMessage(content)
                .build();

        try {
            //noinspection ResultOfMethodCallIgnored
            messageServiceBlockingStub.sendMessage(message);
            sentMessage = true;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void GetMessages() {
        messageServiceStub.getMessages(Empty.newBuilder().build(), new StreamObserver<Message>() {
            @Override
            public void onNext(Message message) {
                receivedMessage = message.getMessage();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    public boolean successfullySentMessage() {
        return sentMessage;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }
}