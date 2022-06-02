package server.models.modules;

import com.lab6.serialization.SerializationProtos.Dragon;
import com.lab6.serialization.SerializationProtos.DragonCollection;
import com.lab6.serialization.SerializationProtos.Response;
import javafx.util.Pair;
import server.ServerManager;
import server.models.comparators.SizeComparator;
import server.models.elements.ServerDragon;
import server.models.enumerations.ServerCommand;
import server.models.enumerations.fields.DradonField;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Отвечает за обработку команды, полученной от клиента
 */

public class CommandProcessingModule {
    private final Dragon command;
    private final SocketChannel channel;

    public CommandProcessingModule(Dragon command, SocketChannel channel) {
        this.command = command;
        this.channel = channel;
    }

    public void process() {
        /*
          Перебираем команды для того, чтобы определить, какую из них выполнять
         */
        for (ServerCommand value : ServerCommand.values()) {

            if (command.getComname().equals(value.name)) {
                /*
                Выполнение команды и формирование отклика
                Команда возвращает сообщение и опционально - коллекцию драконов.
                 */
                Pair<String, Set<ServerDragon>> resp = value.func(ServerManager.DRAGONS, command);
                Response.Builder response = Response.newBuilder();

                if (resp.getKey() != null) response.setResponse(resp.getKey());
                /*
                  Преобразование обычной коллекции в protobuf.
                 */
                if (resp.getValue() != null) {
                    DragonCollection.Builder collection = DragonCollection.newBuilder();
                    List<ServerDragon> sendDragons = new ArrayList<>(resp.getValue());
                    sendDragons.sort(new SizeComparator());

                    for (ServerDragon serverDragon : sendDragons) {
                        Dragon.Builder dragon = Dragon.newBuilder();
                        for (DradonField field : DradonField.values()) {
                            field.setSerialized(serverDragon, dragon);
                        }
                        collection.addDragons(dragon);

                    }
                    response.setCollection(collection);
                }
                ResponseSendingModule sendingModule = new ResponseSendingModule(response.build(), channel);
                sendingModule.start();
            }
        }

    }

}
