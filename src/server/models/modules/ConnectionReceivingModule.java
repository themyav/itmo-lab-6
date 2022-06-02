package server.models.modules;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.*;

/**
 * Отвечает за установку соединения с клиентом
 */

public class ConnectionReceivingModule {
    private final SelectionKey key;
    private final Selector selector;

    public ConnectionReceivingModule(SelectionKey key, Selector selector) {
        this.key = key;
        this.selector = selector;
    }

    public void acceptConnection() {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;

        try {
            channel = serverChannel.accept();
            Server.isAccepted = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (channel == null) return;

        try {
            channel.configureBlocking(false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        Server.logger.debug("Сервер соединен с: " + remoteAddr);

        try {
            channel.register(this.selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            System.out.println(e.getMessage());
        }
    }
}
