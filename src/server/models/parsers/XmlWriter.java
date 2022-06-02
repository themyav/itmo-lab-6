package server.models.parsers;
import server.models.enumerations.fields.CoordinatesField;
import server.models.enumerations.fields.DradonField;
import server.models.enumerations.fields.PersonField;
import server.models.interfaces.Fieldable;
import server.models.elements.ServerDragon;
import server.models.elements.ParsedObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * The type Xml writer.
 */
public class XmlWriter extends XmlParser {
    /**
     * The Writer.
     */
    private FileOutputStream writer;

    /**
     * Создает новый Xml writer.
     *
     * @param dragons  коллекция, элементы которой нужно записать в файл.
     * @param filePath путь к файлу, в который нужно записать коллекцию
     */
    public XmlWriter(Set<ServerDragon> dragons, String filePath) {
        super(dragons, filePath);
    }

    /**
     * Записывает текст в файл.
     *
     * @param text текст, который необходимо записать в файл
     */
    public void write(String text) {

        byte[] buffer = text.getBytes();
        try {
            if (writer != null) {
                writer.write(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Генерирует открывающий xml-тег с заданным текстом и записывает его в файл.
     *
     * @param s the s
     */
    public void openTag(String s) {
        if(s == null) s = "";
        write("<" + s + ">");
    }

    /**
     * Генерирует закрывающий xml-тег с заданным текстом и записывает его в файл.
     *
     * @param s the s
     */
    public void closeTag(String s) {
        if(s == null) s = "";
        write("</" + s + ">\n");
    }

    /**
     * Генерирует содержимое для xml-тега из заданного текста и записывает его в файл.
     *
     * @param s the s
     */
    public void tagData(String s) {
        if(s == null) s = "";
        write(s);
    }

    /**
     * Присваивает значение полю writer.
     */
    public void setWriter(){
        try {
            writer = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Записывает в формате xml все поля объекта.
     *
     * @param <O>    тип объекта, значения полей которого сохраняются
     * @param <T>    тип данных, соответсвующий enumeration, хранящему поля объекта
     * @param object объект, поля которого нужно записать в файл.
     * @param values значения enumeration, соответствующие полям заданного класса.
     */
    public <O extends ParsedObject<O, T>, T extends Enum<T> & Fieldable<O>> void writeObject(O object, T[] values){
        for(T value : values){
            if(value.get(object) == null) continue;
            openTag(value.name());
            if(value.name().equals("COORDINATES")){
                writeObject(value.get(object), CoordinatesField.values());
            }
            else if(value.name().equals("KILLER")){
                writeObject(value.get(object), PersonField.values());
            }
            else tagData(value.get(object).toString());
            closeTag(value.name());
        }
    }

    /**
     * Записывает элементы коллекции в файл.
     */
    public void writeData() {

        setWriter();
        write("<DRAGONS>\n");
        for (ServerDragon dragon : dragons) {
            write("<DRAGON>\n");
            writeObject(dragon, DradonField.values());
            write("</DRAGON>\n");
        }
        write("</DRAGONS>\n");
    }
}
