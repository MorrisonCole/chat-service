import com.google.protobuf.Empty;
import com.morrisoncole.chat.MessageOuterClass.Message;
import com.morrisoncole.chat.MessageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

class ChatClient {

    private final ManagedChannel channel;

    private final MessageServiceGrpc.MessageServiceStub messageServiceStub;
    private final MessageServiceGrpc.MessageServiceBlockingStub messageServiceBlockingStub;

    public ChatClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private ChatClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        messageServiceBlockingStub = MessageServiceGrpc.newBlockingStub(channel);
        messageServiceStub = MessageServiceGrpc.newStub(channel);
    }

    public void SendMessage(String message) {
        Empty response = messageServiceBlockingStub.sendMessage(Message.newBuilder()
                .setMessage(message)
                .build());
    }
}