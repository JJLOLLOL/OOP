package models;

public class Furniture implements ActivityInterface {
    private String name;

    public Furniture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
