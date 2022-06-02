package server.models.elements;

import com.lab6.serialization.SerializationProtos;
import org.w3c.dom.Node;
import server.models.interfaces.Fieldable;

/**
 * The type Parsed object.
 *
 * @param <O> the type parameter
 * @param <T> the type parameter
 */
public abstract class ParsedObject<O extends ParsedObject<O, T>, T extends Enum<T> & Fieldable<O>> {

    /**
     * Instantiates a new Parsed object.
     */
    public ParsedObject() {
    }

    /**
     * Cast this o.
     *
     * @return the o
     */
    O castThis() {
        return (O) this;
    }

    /**
     * Sets fields.
     *
     * @param node   вершина xml-дерева
     * @param fields список полей данного объекта.
     * @throws Exception the exception
     */
    public final void setFields(Node node, T[] fields) throws Exception {
        String nodeValue = node.getTextContent();
        String nodeName = node.getNodeName();
        for (T field : fields) {
            if (nodeName.equals(field.name())) {
                field.set(castThis(), nodeValue);
            }
        }
    }

    /**
     * Считывает значения всех полей элемента.
     *
     * @param fields the fields
     */
    public final void readElement(T[] fields, SerializationProtos.Dragon dragon) {
        for (T field : fields) {
            field.setServer((ServerDragon) castThis(), dragon);
        }
    }

    /**
     * Проверяет поля объекта на соответсвие требованиям.
     *
     * @param fields список полей объекта
     * @throws Exception исключение, указывающее, что поле, которое не может быть null, имеет значение null
     */
    public void checkElement(T[] fields) throws Exception {
        O object = castThis();
        for (T field : fields) {
            if (field.get(object) == null && !field.getNullValid()) {
                throw new Exception(String.format("Значение %s не может быть null.", field.name()));
            }
        }
    }

}
