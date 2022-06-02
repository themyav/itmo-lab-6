package server.models.elements;

import server.ServerManager;
import server.models.enumerations.Color;
import server.models.enumerations.DragonCharacter;
import server.models.enumerations.DragonType;
import server.models.enumerations.fields.DradonField;

import java.time.ZonedDateTime;

/**
 * The type Dragon.
 */
public class ServerDragon extends ParsedObject<ServerDragon, DradonField> implements Comparable<ServerDragon> {
    /**
     * The Id.
     */
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * The Name.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * The Coordinates.
     */
    private Coordinates coordinates; //Поле не может быть null
    /**
     * The Creation date.
     */
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * The Age.
     */
    private int age; //Значение поля должно быть больше 0
    /**
     * The Color.
     */
    private Color color; //Поле может быть null
    /**
     * The Type.
     */
    private DragonType type; //Поле может быть null
    /**
     * The Character.
     */
    private DragonCharacter character; //Поле не может быть null
    /**
     * The Killer.
     */
    private Person killer; //Поле может быть null


    /**
     * Instantiates a new Dragon.
     */
    public ServerDragon() {
        super();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets character.
     *
     * @return the character
     */
    public DragonCharacter getCharacter() {
        return this.character;
    }

    /**
     * Sets character.
     *
     * @param character the character
     */
    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Sets coordinates.
     *
     * @param coordinates the coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public DragonType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(DragonType type) {
        this.type = type;
    }

    /**
     * Gets killer.
     *
     * @return the killer
     */
    public Person getKiller() {
        return killer;
    }

    /**
     * Sets killer.
     *
     * @param killer the killer
     */
    public void setKiller(Person killer) {
        this.killer = killer;
    }

    /**
     * Возвращает строковое представление дракона.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.format("Дракон; id = %s, имя = %s, координаты: %s, дата создания = %s, возраст = %s\n", getId(), getName(), coordinates.toString(), getCreationDate(), getAge()) +
                String.format("Цвет = %s, тип = %s, характер = %s, убийца = %s\n", getColor(), getType(), getCharacter(), (getKiller() == null ? "null" : getKiller().toString()));
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creation date.
     *
     * @param date the date
     */
    public void setCreationDate(ZonedDateTime date) {
        this.creationDate = date;
    }

    /**
     * Сравнивает данного дракона с другим и возвращает 1, если он больше, 0, если они равны, и -1, если он меньше.
     *
     * @param o the o
     * @return the int
     */
    @Override
    public int compareTo(ServerDragon o) {
        return this.getId().compareTo(o.getId());
    }

    /**
     * Возвращает хэш-код дракона
     *
     * @return the int
     */
    public int hashCode() { //нужно для сортировки при добавлении в коллкцию!
        return this.getId();
    }

    /**
     * Производит проверку на то, что все требования к полям соблюдены.
     *
     * @param values список полей дракона
     * @throws Exception исключение, если id не уникально.
     */
    @Override
    public void checkElement(DradonField[] values) throws Exception {
        super.checkElement(values);
        for (int id : ServerManager.dragonIdList) {
            if (id == this.id) throw new Exception("Значение id дракона должно быть уникальным!");
        }
    }
}

