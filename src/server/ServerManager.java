package server;

import server.models.elements.ServerDragon;
import server.models.modules.Server;
import server.models.parsers.XmlReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс Main
 */
public class ServerManager {
    public static final Set<ServerDragon> DRAGONS = new HashSet<>();
    public static List<Integer> dragonIdList = new ArrayList<>();
    public static List<String> personIdList = new ArrayList<>();
    /**
     * Имя XML-файла, содержащего данные о коллекции.
     */
    public static String inputFile = null;

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8080;


    /**
     * Входная точка приложения.
     *
     * @param arg входные данные программы
     */
    public static void main(String[] arg) {

        try {
            inputFile = arg[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Вы не указали входной файл!");
            System.exit(0);
        }

        XmlReader inputFileReader = new XmlReader(DRAGONS, inputFile);
        inputFileReader.readData();

        try {
            new Server(HOSTNAME, PORT).startServer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            }
        }

}
