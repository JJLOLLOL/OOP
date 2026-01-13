package models;

public abstract class Character {
    protected String name;
    protected int age;
    protected String gender;
    protected String location;
    //personality traits can be added later
    //cash
    //mood
    //skills

    public Character(String name, int age, String gender) {//playable character
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = "Home"; // This is the default starting location ( This is also to locate the character )
    }
    public Character(String name, int age, String gender, String location) {//for npc
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
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
