package models;

public abstract class Character {
    protected String name;
    protected int agegroup;
    protected String gender;
    protected String location;
    //personality traits can be added later
    //cash
    //mood
    //skills
    //link it back to career

    public Character(String name, int agegroup , String gender) {//playable character
        this.name = name;
        this.agegroup = agegroup;
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
