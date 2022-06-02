package server.models.interfaces;

/**
 * Абстрактный интерфейс InputProcessor
 *
 * @param <O> тип объекта, поле которого будет присваиваться
 */
public interface InputProcessor<O> {
    /**
     * Обрабатывает значение и в случае его корректности присваивает это значение полю.
     *
     * @param object объект, значение поля которого нужно присвоить
     * @param s      значение, которое нужно присвоить
     * @throws Exception исключение в случае значения, не соответсвующего требованиям
     */
    void processInput(O object, String... s) throws Exception;
}
