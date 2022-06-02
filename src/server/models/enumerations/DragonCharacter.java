package server.models.enumerations;

/**
 * Список характеров, допустимых для дракона.
 */
public enum DragonCharacter implements Comparable<DragonCharacter> {
    /**
     * Cunning dragon character.
     */
    CUNNING,
    /**
     * Wise dragon character.
     */
    WISE,
    /**
     * Good dragon character.
     */
    GOOD,
    /**
     * Chaotic dragon character.
     */
    CHAOTIC;

    /*@Override
    public int compareTo(DragonCharacter character){
        return this.name().compareTo(character.name());
    }*/
    //compareTo
}
