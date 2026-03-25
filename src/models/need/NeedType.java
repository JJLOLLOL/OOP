package models.need;

public enum NeedType {
    HUNGER("Hunger"),
    HYGIENE("Hygiene"),
    ENERGY("Energy"),
    FUN("Fun"),
    SOCIAL("Social");
    

    private final String name;

    NeedType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NeedType getType(String name) {
        return NeedType.valueOf(name);
    }
}