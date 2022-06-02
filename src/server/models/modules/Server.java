package server.models.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ServerManager;
import server.models.parsers.XmlWriter;
import sun.misc.Signal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


public class Server {

    public static Logger logger;
    public static boolean isAccepted = false;
    private final InetSocketAddress listenAddress;


    public Server(String address, int port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);
        logger = LoggerFactory.getLogger(address + "logger");
    }

    private static void Saver() {
        Signal.handle(new Signal("TSTP"), sig -> {
            XmlWriter writer = new XmlWriter(ServerManager.DRAGONS, ServerManager.inputFile);
            writer.writeData();
            logger.debug("Коллекция сохранена в файл по команде ctrl-z.");
        });
    }

    public void startServer() throws IOException {
        Saver();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(listenAddress);
        serverChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        logger.debug("Сервер запущен на порте: >> " + listenAddress.getPort());

        String ex = "";


        while (!Objects.equals(ex, "exit")) {
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    ConnectionReceivingModule connectionReceivingModule = new ConnectionReceivingModule(key, selector);
                    connectionReceivingModule.acceptConnection();
                } else if (key.isReadable()) {
                    RequestReaderModule requestReaderModule = new RequestReaderModule(key);
                    requestReaderModule.readRequest();
                }
            }
            if (!isAccepted) {

                XmlWriter writer = new XmlWriter(ServerManager.DRAGONS, ServerManager.inputFile);
                writer.writeData();
                logger.debug("Коллекция сохранена в файл");

            }

        }
    }
}