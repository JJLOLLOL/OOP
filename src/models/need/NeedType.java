package models.need;

/**
 * Enumerates the needs tracked for each sim.
 */
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

}
