package server.models.elements;

import server.ServerManager;
import server.models.enumerations.fields.PersonField;

/**
 * The type Person.
 */
public class Person extends ParsedObject<Person, PersonField> {
    /**
     * The Name.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * The Birthday.
     */
    private java.time.LocalDate birthday; //Поле может быть null
    /**
     * The Height.
     */
    private Double height; //Поле может быть null, Значение поля должно быть больше 0
    /**
     * The Weight.
     */
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0
    /**
     * The Passport id.
     */
    private String passportID; //Строка не может быть пустой, Длина строки должна быть не меньше 9, Значение этого поля должно быть уникальным, Поле может быть null

    /**
     * Instantiates a new Person.
     */
    public Person() {
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get birthday java . time . local date.
     *
     * @return the java . time . local date
     */
    public java.time.LocalDate getBirthday() {
        return this.birthday;
    }

    /**
     * Set birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(java.time.LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public Double getHeight() {
        return this.height;
    }

    /**
     * Set height.
     *
     * @param height the height
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * Get weight float.
     *
     * @return the float
     */
    public Float getWeight() {
        return this.weight;
    }

    /**
     * Set weight.
     *
     * @param weight the weight
     */
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    /**
     * Get passport id string.
     *
     * @return the string
     */
    public String getPassportID() {
        return this.passportID;
    }

    /**
     * Set passport id.
     *
     * @param passportID the passport id
     */
    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    /**
     * Выводит строковое представление человека.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.format("имя = %s, день рождения = %s, рост = %s, вес = %s, id паспорта = %s", name, birthday, height, weight, passportID);
    }

    /**
     * Check element.
     *
     * @param values список полей человека
     * @throws Exception исключение, указывающее, что значение passportID не уникально.
     */
    @Override
    public void checkElement(PersonField[] values) throws Exception {
        super.checkElement(values);
        for (String id : ServerManager.personIdList) {
            if (id.equals(this.passportID)) throw new Exception("Значение passportID должно быть уникальным!");
        }
    }


}
