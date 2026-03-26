package testTypes;

public enum Gender {
    MALE("Male", "M"),
    FEMALE("Female", "F");

    private final String displayName;
    private final String label;

    Gender(String displayName, String label) {
        this.displayName = displayName;
        this.label = label;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLabel() {
        return label;
    }

    public static Gender fromUserInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Gender input cannot be null.");
        }

        return switch (input.trim().toUpperCase()) {
            case "M", "MALE" -> MALE;
            case "F", "FEMALE" -> FEMALE;
            default -> throw new IllegalArgumentException("Invalid gender input: " + input);
        };
    }

    public static Gender fromDataValue(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Gender data value cannot be null.");
        }

        return switch (input.trim().toUpperCase()) {
            case "M", "MALE" -> MALE;
            case "F", "FEMALE" -> FEMALE;
            default -> throw new IllegalArgumentException("Unknown gender value: " + input);
        };
    }
}