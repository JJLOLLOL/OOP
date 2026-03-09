package models;

public abstract class Character {
    private String name;
    private int age;
    private String gender;
    private Location location;

    public Character(String name, int age , String gender, Location defaultLocation) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = defaultLocation;
    }

    // getters & setters
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
}