package models;

/**
 * Abstract base class representing a character in the game world.
 * Provides common attributes such as name, age, gender, and the current location.
 */
public abstract class Character {
    private final String name;
    private final int age;
    private final String gender;
    private Location location;

    /**
     * Constructs a new {@code Character}.
     *
     * @param name            the character's name
     * @param age             the character's age
     * @param gender          the character's gender
     * @param defaultLocation the character's starting location
     */
    public Character(String name, int age , String gender, Location defaultLocation) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = defaultLocation;
    }

    /**
     * Retrieves the character's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the character's age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Retrieves the character's gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the character's current location.
     *
     * @return the current {@link Location}
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the character's current location.
     *
     * @param location the new {@link Location}
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}