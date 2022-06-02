package server.models.enumerations.fields;

import com.lab6.serialization.SerializationProtos;
import server.models.elements.Coordinates;
import server.models.elements.ServerDragon;
import server.models.interfaces.Fieldable;

/**
 * Список полей объекта Координаты
 */
public enum CoordinatesField implements Fieldable<Coordinates> {
    /**
     * x-координата
     */
    X() {
        @Override
        public Double get(Coordinates coordinates) {
            return coordinates.getX();
        }

        @Override
        public void set(Coordinates coordinates, String... args) throws Exception {
            if (args.length == 0) return;
            double x = Double.parseDouble(args[0]);
            if (x > 280) throw new Exception(String.format("Значение поля %s не может быть больше 280!", this.name()));
            coordinates.setX(x);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }
    },
    /**
     * y-координата
     */
    Y() {
        @Override
        public Float get(Coordinates coordinates) {
            return coordinates.getY();
        }

        @Override
        public void set(Coordinates coordinates, String... args) {
            if (args.length == 0) return;
            float y = Float.parseFloat(args[0]);
            coordinates.setY(y);
        }


        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }

    };

    /**
     * Instantiates a new Coordinates field.
     */
    CoordinatesField() {

    }

    /**
     * Возвращает значение true, если поле может быть null, и false иначе.
     *
     * @return the null valid
     */
    @Override
    public boolean getNullValid() {
        return true;
    }

}
