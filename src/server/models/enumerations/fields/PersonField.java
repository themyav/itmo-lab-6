package server.models.enumerations.fields;

import com.lab6.serialization.SerializationProtos;
import server.ServerManager;
import server.models.elements.Person;
import server.models.elements.ServerDragon;
import server.models.interfaces.Fieldable;

import java.time.LocalDate;

/**
 * Список полей объекта Person
 */
public enum PersonField implements Fieldable<Person> {
    /**
     * Имя человека.
     */
    NAME(false) {
        @Override
        public String get(Person person) {
            return person.getName();
        }

        @Override
        public void set(Person person, String... args) throws Exception {
            if (args.length == 0) throw new Exception("Поле не может иметь значение null!");
            person.setName(args[0]);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }

    },
    /**
     * Дата рождения человека.
     */
    BIRTHDAY(true) {
        @Override
        public LocalDate get(Person person) {
            return person.getBirthday();
        }

        @Override
        public void set(Person person, String... args) throws Exception {

            LocalDate localDate = LocalDate.parse(args[0]);
            person.setBirthday(localDate);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }

    },
    /**
     * Рост человека.
     */
    HEIGHT(true) {
        @Override
        public Double get(Person person) {
            return person.getHeight();
        }

        @Override
        public void set(Person person, String... args) throws Exception {
            if (args.length == 0) return;
            double height = Double.parseDouble(args[0]);
            if (height <= 0) throw new Exception("Рост должен быть больше 0!");
            person.setHeight(height);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }
    },
    /**
     * Вес человека.
     */
    WEIGHT(true) {
        @Override
        public Float get(Person person) {
            return person.getWeight();
        }

        @Override
        public void set(Person person, String... args) throws Exception {
            if (args.length == 0) return;
            float weight = Float.parseFloat(args[0]);
            if (weight <= 0) throw new Exception("Вес должен быть больше 0!");
            person.setWeight(weight);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }
    },
    /**
     * Номер паспорта человека.
     */
    PASSPORTID(true) {
        @Override
        public String get(Person person) {
            return person.getPassportID();
        }

        @Override
        public void set(Person person, String... args) throws Exception {
            if (args.length == 0) return;
            if (args[0].length() < 9) throw new Exception("Длина строки меньше 9!");
            for (String id : ServerManager.personIdList) {
                if (args[0].equals(id)) throw new Exception("Значение id должно быть уникальным!");
            }
            person.setPassportID(args[0]);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {

        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {

        }
    };
    /**
     * Значение, указывающее, может ли поле быть null.
     */
    private final boolean nullValid;

    /**
     * Instantiates a new Person field.
     *
     * @param nullValid the null valid
     */
    PersonField(boolean nullValid) {
        this.nullValid = nullValid;
    }

    /**
     * Возвращает значение true, если поле может быть null, и false иначе.
     *
     * @return the boolean
     */
    @Override
    public boolean getNullValid() {
        return nullValid;
    }

}
