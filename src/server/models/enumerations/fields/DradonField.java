package server.models.enumerations.fields;

import com.lab6.serialization.SerializationProtos;
import server.models.elements.Coordinates;
import server.models.elements.Person;
import server.models.elements.ServerDragon;
import server.models.enumerations.Color;
import server.models.enumerations.DragonCharacter;
import server.models.enumerations.DragonType;
import server.models.interfaces.Fieldable;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static java.lang.Math.abs;

/**
 * Список полей объекта Дракон
 */
public enum DradonField implements Fieldable<ServerDragon> {
    /**
     * Имя дракона.
     */
    NAME(false) {
        @Override
        public String get(ServerDragon dragon) {
            return dragon.getName();
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            if (args.length == 0 || args[0] == null) throw new Exception("Имя дракона не может быть пустым!");
            dragon.setName(args[0]);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) dragon.setName(this.get(serverDragon));
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            if (dragon.hasName()) {
                String name = dragon.getName();
                try {
                    this.set(serverDragon, name);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    },
    /**
     * Координаты дракона.
     */
    COORDINATES(false) {
        @Override
        public Coordinates get(ServerDragon dragon) {
            return dragon.getCoordinates();
        }

        @Override
        public void set(ServerDragon dragon, String... args) {
            Coordinates coordinates = new Coordinates();
            //coordinates.readElement(CoordinatesField.values()); //!!
            dragon.setCoordinates(coordinates);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                Coordinates serverCoordinates = this.get(serverDragon);
                dragon.setX(Double.toString(serverCoordinates.getX()));
                dragon.setY(Float.toString(serverCoordinates.getY()));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            Coordinates serverCoordinates = new Coordinates();
            serverCoordinates.setX(Double.parseDouble(dragon.getX()));
            serverCoordinates.setY(Float.parseFloat(dragon.getY()));
            serverDragon.setCoordinates(serverCoordinates);
        }


    },
    /**
     * Дата создания дракона.
     */
    CREATIONDATE(false) {
        @Override
        public ZonedDateTime get(ServerDragon dragon) {
            return dragon.getCreationDate();
        }

        @Override
        public void set(ServerDragon dragon, String... args) {
            if (args.length == 0) {
                if (dragon.getCreationDate() != null) return; //обновление дракона
                dragon.setCreationDate(ZonedDateTime.now());
            } else {
                try {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(args[0]);
                    dragon.setCreationDate(zonedDateTime);
                } catch (Exception e) {
                    System.out.println("В файле задана некорректная дата создания дракона!");
                    System.exit(0);
                }
            }
        }

        @Override
        public boolean getNullValid() {
            return nullValid;
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setCreationDate(this.get(serverDragon).toString());
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            this.set(serverDragon); //автоматическое
        }


    },
    /**
     * Возраст дракона.
     */
    AGE(false) {
        @Override
        public Integer get(ServerDragon dragon) { //TODO think
            return dragon.getAge();
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            for (String value : args) {
                int age = Integer.parseInt(value); //TODO overflow?
                if (age <= 0) throw new Exception("Возраст должен быть больше 0!");
                dragon.setAge(age);
                break;
            }
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setAge(this.get(serverDragon));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            if (dragon.hasAge()) {
                serverDragon.setAge(dragon.getAge());
            }
        }

    },
    /**
     * Цвет дракона.
     */
    COLOR(true) {
        @Override
        public Color get(ServerDragon dragon) {
            return dragon.getColor();
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            if (args.length == 0) {
                dragon.setColor(null);
                return;
            }
            for (Color color : Color.values()) {
                if (args[0].equals(color.name())) {
                    dragon.setColor(color);
                    return;
                }
            }
            throw new Exception(String.format("Цвета дракона %s нет", args[0]));
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setColor(SerializationProtos.Dragon.Color.valueOf(this.get(serverDragon).name()));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            if (dragon.hasColor()) {
                try {
                    this.set(serverDragon, dragon.getColor().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    },
    /**
     * Тип дракона.
     */
    TYPE(true) {
        @Override
        public DragonType get(ServerDragon dragon) {
            return dragon.getType();
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            if (args.length == 0) {
                dragon.setType(null);
                return;
            }
            for (DragonType type : DragonType.values()) {
                if (args[0].equals(type.name())) {
                    dragon.setType(type);
                    return;
                }
            }
            throw new Exception(String.format("Типа дракона %s нет!", args[0]));
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setType(SerializationProtos.Dragon.DragonType.valueOf(this.get(serverDragon).name()));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            if (dragon.hasType()) {
                try {
                    this.set(serverDragon, dragon.getType().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    },
    /**
     * Характер дракона.
     */
    CHARACTER(false) {
        @Override
        public DragonCharacter get(ServerDragon dragon) {
            return dragon.getCharacter();
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            if (args.length == 0) throw new Exception("Вы не ввели характер дракона!");
            for (DragonCharacter character : DragonCharacter.values()) {
                if (args[0].equals(character.name())) {
                    dragon.setCharacter(character);
                    return;
                }
            }
            throw new Exception(String.format("Типа дракона %s нет!", args[0]));
        }


        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setCharacter(SerializationProtos.Dragon.DragonCharacter.valueOf(this.get(serverDragon).name()));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            if (dragon.hasType()) {
                try {
                    this.set(serverDragon, dragon.getCharacter().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    },
    /**
     * Убийца дракона.
     */
    KILLER(true) {
        @Override
        public Person get(ServerDragon dragon) {
            return dragon.getKiller();
        }

        @Override
        public void set(ServerDragon dragon, String... args) {
            Person killer = new Person();
            //killer.readElement(PersonField.values());
            dragon.setKiller(killer);
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                Person killer = this.get(serverDragon);
                if (killer.getName() != null) dragon.setKillerName(killer.getName());
                if (killer.getBirthday() != null) dragon.setKillerBirthday(killer.getBirthday().toString());
                if (killer.getHeight() != null) dragon.setKillerHeight(killer.getHeight().toString());
                if (killer.getWeight() != null) dragon.setKillerWeight(killer.getWeight().toString());
                if (killer.getPassportID() != null) dragon.setKillerPassportId(killer.getPassportID());
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            Person serverKiller = new Person();
            if (dragon.hasKillerName()) serverKiller.setName(dragon.getKillerName());
            if (dragon.hasKillerBirthday()) serverKiller.setBirthday(LocalDate.parse(dragon.getKillerBirthday()));
            if (dragon.hasKillerHeight()) serverKiller.setHeight(Double.parseDouble(dragon.getKillerHeight()));
            if (dragon.hasKillerWeight()) serverKiller.setWeight(Float.parseFloat(dragon.getKillerWeight()));
            if (dragon.hasKillerPassportId()) serverKiller.setPassportID(dragon.getKillerPassportId());

            serverDragon.setKiller(serverKiller);
        }
    },
    /**
     * Id дракона.
     */
    ID(false) {
        @Override
        public Integer get(ServerDragon object) {
            return object.getId();
        }

        private void generateID(ServerDragon dragon) {
            if (dragon.getId() != null) return; //id уже сгенерировано, обновляем эл-т
            int power = 1, id = 0;
            for (DradonField field : DradonField.values()) {
                if (field.name().equals("ID") || field.get(dragon) == null) continue;
                id += power * field.get(dragon).hashCode();
                power *= 27;
            }
            dragon.setId(abs(id));
        }

        @Override
        public void set(ServerDragon dragon, String... args) throws Exception {
            if (args.length == 0) generateID(dragon);
            else {
                int id = Integer.parseInt(args[0]);
                if (id <= 0) throw new Exception("id дракона должно быть больше 0");
                dragon.setId(Integer.parseInt(args[0]));
            }
        }

        @Override
        public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
            if (this.get(serverDragon) != null) {
                dragon.setId(this.get(serverDragon));
            }
        }

        @Override
        public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
            try {
                this.set(serverDragon); //автоматическое
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    };


    /**
     * Значение, указывающее, может ли поле быть null.
     */
    protected final boolean nullValid;


    /**
     * Instantiates a new Dradon field.
     *
     * @param nullValid the null valid
     */
    DradonField(boolean nullValid) {
        this.nullValid = nullValid;
    }

    /**
     * Gets null valid.
     *
     * @return the null valid
     */
    @Override
    public boolean getNullValid() {
        return nullValid;
    }

    public void setSerialized(ServerDragon serverDragon, SerializationProtos.Dragon.Builder dragon) {
    }

    public void setServer(ServerDragon serverDragon, SerializationProtos.Dragon dragon) {
    }

}
