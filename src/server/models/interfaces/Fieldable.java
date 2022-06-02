package server.models.interfaces;

import com.lab6.serialization.SerializationProtos;
import server.models.elements.ServerDragon;

/**
 * The interface Fieldable.
 *
 * @param <O> the type parameter
 */
public interface Fieldable<O> {

    /**
     * Возвращает значение поля объекта.
     *
     * @param <T>    тип возвращаемого поля
     * @param object объект, поле которого возвращается
     * @return значение поля объекта
     */
    <T> T get(O object);

    /**
     * Устанавливает значение поля объекта
     *
     * @param object объект, поле которого устанавливается
     * @param args   значение поля, которое нужно установить
     * @throws Exception исключение в случае несоответвия значения требованиям
     */
    void set(O object, String... args) throws Exception;

    /**
     * Возвращает значение true, если поле может быть null, и false иначе
     *
     * @return the null valid
     */
    boolean getNullValid();

    void setSerialized(ServerDragon serverDragon, com.lab6.serialization.SerializationProtos.Dragon.Builder dragon);

    void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon);

}

