package server.models.modules;


import com.lab6.serialization.SerializationProtos;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Отвечает за сериализацию команды и отправку ее клиенту
 */
public class ResponseSendingModule {
    private final SerializationProtos.Response response;
    private final SocketChannel channel;

    public ResponseSendingModule(SerializationProtos.Response response, SocketChannel channel) {
        this.response = response;
        this.channel = channel;
    }

    public void start() {
        byte[] serializedResponse = serialize();
        sendResponse(serializedResponse);
    }

    /**
     * Сериализует отклик
     */
    private byte[] serialize() {
        return this.response.toByteArray();
    }

    /**
     * Отправляет отклик клиенту
     */
    private void sendResponse(byte[] response) {
        //System.out.println(response.length);
        try {
            channel.write(ByteBuffer.wrap(response));
            Server.logger.debug("Отправлен отклик клиенту");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
