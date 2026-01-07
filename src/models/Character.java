package models;

public abstract class Character {
    protected String name;
    protected int age;
    protected String gender;
    protected String location;

    public Character(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = "Home"; // This is the default starting location ( This is also to locate the character )
    }

    public String getName() {
        return name;
    }

    public abstract void update(int minutesPassed);

    public abstract void displayInfo();

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
