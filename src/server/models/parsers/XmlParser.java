package server.models.parsers;

import server.models.elements.ServerDragon;

import java.util.Set;

/**
 * Объект-парсер, производящий взаимодейтсвие с xml-файлом.
 */
public abstract class XmlParser {
    /**
     * Путь к xml-файлу.
     */
    protected String filePath;
    /**
     * Коллекция, которую необходимо считать либо записать в файл.
     */
    protected Set<ServerDragon> dragons;

    /**
     * Создает новый парсер
     *
     * @param dragons  коллекция драконов
     * @param filePath путь к xml-файлу
     */
    public XmlParser(Set<ServerDragon> dragons, String filePath){
        this.dragons = dragons;
        this.filePath = filePath;
    }
}
