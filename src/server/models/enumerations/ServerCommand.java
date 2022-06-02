package server.models.enumerations;

import com.lab6.serialization.SerializationProtos.Dragon;
import javafx.util.Pair;
import server.ServerManager;
import server.models.elements.ServerDragon;
import server.models.enumerations.fields.DradonField;
import server.models.parsers.XmlWriter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Список команд, которые может выполнять программа.
 */
public enum ServerCommand {

    /**
     * Команда Help.
     */
    HELP("help", "- справка по доступным командам") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            String message = "Выведите одну из следующих команд:\n" + Arrays.stream(ServerCommand.values()).map(ServerCommand::toString).collect(Collectors.joining("\n"));
            return new Pair<>(message, null);
        }
    },
    /**
     * Команда Info.
     */
    INFO("info", "- вывести информацию о коллекции") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            String message = "Информация о коллекции\n";
            message += "Тип коллекции: " + dragons.getClass().getTypeName();
            message += "\nРазмер коллекции: " + dragons.size();
            /*String message = "\nИнформация о коллекции:\n" + String.format("Тип коллекции %s\n", dragons.getClass().getTypeName().toString()) +
                    String.format("Размер коллекции %s\n", dragons.size());*/
            return new Pair<>(message, null);

        }
    },
    /**
     * Команда Show.
     */
    SHOW("show", "- вывести все элементы коллекции") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            return new Pair<>("В коллекции содержатся следующие элементы:", dragons);
        }
    },

    ADD("add", "{element} - добавить новый элемент в коллекцию") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {

            ServerDragon dragon = new ServerDragon();
            dragon.readElement(DradonField.values(), command);

            if (dragon.getKiller().getPassportID() != null) {
                long cnt = dragons.stream().filter(x -> (x != null && x.getKiller() != null && x.getKiller().getPassportID() != null && x.getKiller().getPassportID().equals(dragon.getKiller().getPassportID()))).count();
                if (cnt > 0)
                    return new Pair<>(String.format("Невозможно добавить дракона. Элемент с passport id убийцы = %s уже есть в коллекции", dragon.getKiller().getPassportID()), null);
            }
            dragons.add(dragon);
            return new Pair<>("В коллекцию добавлен новый элемент", null);
        }
    },
    EXECUTE_SCRIPT("execute_script", " script - исполнить скрипт") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {


            return new Pair<>("Made script", null);
        }

    },
    /**
     * Команда Update.
     */
    UPDATE("update", "id {element} - обновить значение элемента коллекции, id которого равен заданному") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {

            final Integer id = Integer.parseInt(command.getArguments());
            ServerDragon dragon = new ServerDragon();
            dragon.readElement(DradonField.values(), command);

            if (dragon.getKiller().getPassportID() != null) {
                long cnt = dragons.stream().filter(x -> (x != null && x.getKiller() != null && x.getKiller().getPassportID() != null && x.getKiller().getPassportID().equals(dragon.getKiller().getPassportID()))).count();
                if (cnt > 0)
                    return new Pair<>(String.format("Невозможно добавить дракона. Элемент с passport id убийцы = %s уже есть в коллекции", dragon.getKiller().getPassportID()), null);
            }

            ServerDragon oldServerDragon = dragons.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (oldServerDragon == null)
                return new Pair<>(String.format("Элемент с id = %s отсутствует в коллекции\n", id), null);
            else {
                dragons.remove(oldServerDragon);
                dragons.add(dragon);
                return new Pair<>(String.format("Элемент с id = %s удален\n", id), null);
            }
        }
    },
    /**
     * Команда Remove by id.
     */
    REMOVE_BY_ID("remove_by_id", "id - удалить элемент из коллекции по его id") {
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            final Integer id = Integer.parseInt(command.getArguments());
            ServerDragon delServerDragon = dragons.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (delServerDragon == null)
                return new Pair<>(String.format("Элемент с id = %s отсутствует в коллекции\n", id), null);
            else return new Pair<>(String.format("Элемент с id = %s удален\n", id), null);
        }
    },
    /**
     * Команда Clear.
     */
    CLEAR("clear", "- очистить коллекцию") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            dragons.clear();
            return new Pair<>("Коллекция очищена", null);
        }
    },
    /**
     * Команда Save.
     */
    SAVE("save", "- сохранить коллекцию в файл [Недоступна клиенту]") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            XmlWriter writer = new XmlWriter(dragons, ServerManager.inputFile);
            writer.writeData();
            return new Pair<>("Коллекция сохранена в файл", null);

        }
    },
    /**
     * Команда Exit.
     */
    EXIT("exit", "- завершить работу клиентского приложения.") {
        /**
         * Завершает программу без сохранения в файл
         *  @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/

        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            return ServerCommand.SAVE.func(dragons, command);
        }
    },
    /**
     * Команда Add if min.
     */
    ADD_IF_MIN("add_if_min", "{element} - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции") {
        /**
         * Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
         *  @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            ServerDragon dragon = new ServerDragon();
            dragon.readElement(DradonField.values(), command);


            if (Collections.min(dragons).compareTo(dragon) > 0) {

                if (dragon.getKiller().getPassportID() != null) {
                    long cnt = dragons.stream().filter(x -> (x != null && x.getKiller() != null && x.getKiller().getPassportID() != null && x.getKiller().getPassportID().equals(dragon.getKiller().getPassportID()))).count();
                    if (cnt > 0)
                        return new Pair<>(String.format("Невозможно добавить дракона. Элемент с passport id убийцы = %s уже есть в коллекции", dragon.getKiller().getPassportID()), null);
                }
                dragons.add(dragon);
                return new Pair<>("В коллекцию добавлен новый элемент", null);
            } else return new Pair<>("В коллекцию НЕ добавлен новый элемент", null);
        }
    },
    /**
     * Команда Remove greater.
     */
    REMOVE_GREATER("remove_greater", "{element} - удалить из коллекции все элементы, превышающие заданный") {
        /**
         * Удаляет из коллекции все элементы, превышающие заданный
         *  @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            ServerDragon newDragon = new ServerDragon();
            newDragon.readElement(DradonField.values(), command);
            dragons.removeIf(x -> x.compareTo(newDragon) > 0);
            return new Pair<>("Объекты успешно удалены из коллекции", null);
        }
    },
    /**
     * Команда Remove lower.
     */
    REMOVE_LOWER("remove_lower", "{element} - удалить из коллекции все элементы, меньшие, чем заданный") {
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            ServerDragon newDragon = new ServerDragon();
            newDragon.readElement(DradonField.values(), command);
            dragons.removeIf(x -> x.compareTo(newDragon) < 0);
            return new Pair<>("Объекты успешно удалены из коллекции", null);
        }
    },
    /**
     * Команда Sum of age.
     */
    SUM_OF_AGE("sum_of_age", "- вывести сумму значений поля age для всех элементов коллекции") {
        /**
         * Выводит сумму значений поля age для всех элементов коллекции.
         * @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            long sum = dragons.stream().mapToInt(ServerDragon::getAge).sum();
            return new Pair<>(String.format("Сумма полей age элементов sum = %s%n", sum), null);
        }
    },
    /**
     * Команда Count greater than character.
     */
    COUNT_GREATER_THAN_CHARACTER("count_greater_than_character", "character - вывести количество элементов, значение поля character которых больше заданного") {
        /**
         * Выводит количество элементов, значение поля character которых больше заданного
         *  @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            DragonCharacter character = chooseCharacter(command.getArguments()); //null гарантированно нет
            if (character == null) return new Pair<>("Такого характера нет!", null);
            long counter = dragons.stream().map(x -> x.getCharacter().compareTo(character) > 0).count();
            String message = String.format("Количество драконов с полем character большим %s равно %s\n", character, counter);
            return new Pair<>(message, null);
        }
    },
    /**
     * Команда Filter by character.
     */
    FILTER_BY_CHARACTER("filter_by_character", "character - вывести элементы, значение поля character которых равно заданному") {
        /**
         * Выводит элементы, значение поля character которых равно заданному
         *  @param dragons коллекция, к которой нужно применить команду
         * @param command    аргументы команды
         * @return*/
        @Override
        public Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command) {
            DragonCharacter character = chooseCharacter(command.getArguments()); //null гарантированно нет
            if (character == null) return new Pair<>("Такого характера нет!", null);
            HashSet<ServerDragon> greatDragon = dragons.stream().filter(x -> x.getCharacter().equals(character)).collect(Collectors.toCollection(HashSet::new));
            String message = String.format("Список драконов с полем character равным %s\n", character);
            return new Pair<>(message, greatDragon);
        }
    };

    /**
     * Имя команды
     */
    public final String name;
    /**
     * Пояснение к команде
     */
    public final String explanation;
    /**
     * The Reader.
     */
    final Scanner reader;

    /**
     * Создает новую команду
     *
     * @param name        имя команды
     * @param explanation пояснение к команде
     */
    ServerCommand(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
        this.reader = new Scanner(System.in);
    }

    /**
     * Проверяет, какой характер был введенHashS
     *
     * @param curCharacter the cur character
     * @return the dragon character
     */
    private static DragonCharacter chooseCharacter(String curCharacter) {
        DragonCharacter character = null;
        for (DragonCharacter dragonCharacter : DragonCharacter.values()) {
            if (dragonCharacter.name().equals(curCharacter)) {
                character = dragonCharacter;
            }
        }
        return character;
    }

    /**
     * Функция, которую выполняет команда
     *
     * @param dragons коллекция, к которой нужно применить команду
     * @param command аргументы команды
     */

    public abstract Pair<String, Set<ServerDragon>> func(Set<ServerDragon> dragons, Dragon command);


    @Override
    public String toString() {
        return this.name + " " + this.explanation;
    }

}
