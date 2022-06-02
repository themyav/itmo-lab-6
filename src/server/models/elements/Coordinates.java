package server.models.elements;

import server.models.enumerations.fields.CoordinatesField;

/**
 * The type Coordinates.
 */
public class Coordinates extends ParsedObject<Coordinates, CoordinatesField> {
    /**
     * X-координата
     */
    private double x; //Максимальное значение поля: 280
    /**
     * У-координата
     */
    private float y;

    /**
     * Создает новые координаты.
     */
    public Coordinates() {
        super();
    }

    /**
     * Возвращает значение x.
     *
     * @return x-координата
     */
    public double getX() {
        return x;
    }

    /**
     * Устанавливает x.
     *
     * @param x x-координата
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Возвращает значение y.
     *
     * @return y-координата
     */
    public float getY() {
        return y;
    }

    /**
     * Устанавливает y.
     *
     * @param y y-координата
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строка
     */
    @Override
    public String toString() {
        return String.format("x = %s, y = %s", getX(), getY());
    }

    /**
     * Возвращает хэш-код координат.
     *
     * @return целое число
     */
    @Override
    public int hashCode() {
        return (int) (getX() + getY() * 37);
    }


}