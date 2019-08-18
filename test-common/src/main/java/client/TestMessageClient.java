package client;

import com.morrisoncole.chat.MessageOuterClass.Message;
import com.morrisoncole.chat.MessageServiceGrpc;
import com.morrisoncole.chat.MessageServiceGrpc.MessageServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

public class TestMessageClient {

    private static final Logger LOGGER = Logger.getLogger(TestMessageClient.class.getName());

    private final MessageServiceBlockingStub messageServiceBlockingStub;

    private boolean sentMessage = false;

    public TestMessageClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
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

    public boolean successfullySentMessage() {
        return sentMessage;
    }
}