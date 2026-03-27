package types;

/**
 * Supported genders for characters and parser input.
 */
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

    /**
     * Parses a user-entered gender token such as {@code M}, {@code F},
     * {@code male}, or {@code female}.
     *
     * @param input the user input to parse
     * @return the matching gender
     */
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

    /**
     * Parses a gender token coming from data files.
     *
     * @param input the serialized gender value
     * @return the matching gender
     */
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
