package server.models.modules;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lab6.serialization.SerializationProtos.Dragon;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


/**
 * Отвечает за считывание запроса, поступившего от клиента.
 * Десереализует команду и отправляет ее на обработку
 */
public class RequestReaderModule {
    private final SelectionKey key;

    public RequestReaderModule(SelectionKey key) {
        this.key = key;
    }

    /**
     * Десериализует запрос
     *
     * @param request запрос клиента, представленный в виде массива байт
     */
    public static Dragon deserialize(ByteBuffer request, int requestLength) {
        byte[] data = new byte[requestLength];

        System.arraycopy(request.array(), 0, data, 0, requestLength);

        Dragon com = null;
        try {
            com = Dragon.parseFrom(data);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return com;

    }

    public void readRequest() {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int numRead = 0;
        try {
            numRead = channel.read(buffer);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddress = socket.getRemoteSocketAddress();
            Server.logger.debug("Соединение с клиентом прервано: " + remoteAddress);
            try {
                channel.close();
                Server.isAccepted = false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            key.cancel();
            return;
        }
        Dragon command = deserialize(buffer, numRead);
        if (command != null) processCommand(command, channel);
    }

    private void processCommand(Dragon command, SocketChannel channel) {
        Server.logger.debug("Получена команда от клиента: " + command.getComname());
        CommandProcessingModule commandProcessingModule = new CommandProcessingModule(command, channel);
        commandProcessingModule.process();
    }

}
