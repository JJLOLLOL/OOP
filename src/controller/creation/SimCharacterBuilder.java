package controller.creation;

import Types.Gender;
import models.character.SimCharacter;
import models.location.Location;

/**
 * Builder for incrementally creating a {@link SimCharacter} instance.
 * This is used during the sim creation phase to hold in-flight data.
 */
public class SimCharacterBuilder {
    private String name;
    private int age;
    private Gender gender;

    public SimCharacterBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SimCharacterBuilder withAge(int age) {
        this.age = age;
        return this;
    }

    public SimCharacterBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Creates a final {@link SimCharacter} instance from the built data.
     *
     * @param defaultLocation The location to assign to the new Sim.
     * @return A new {@link SimCharacter}.
     */
    public SimCharacter build(Location defaultLocation) {
        return new SimCharacter(name, age, gender, defaultLocation);
    }

    // Accessors for the Renderer to display confirmed sim data
    public String getName() { return name; }

    public int getAge() { return age; }

    public String getGenderDisplayName() { return gender != null ? gender.getDisplayName() : ""; }
}