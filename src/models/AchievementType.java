package models;

public enum AchievementType {
    FIRST_JOB("First Job", "Get your first non-jobless career", false),
    FIRST_COOKING("First Cooking", "Use Cooking skill for the first time", false),
    FIRST_FITNESS("First Fitness", "Use Fitness skill for the first time", false),
    FIRST_PROGRAMMING("First Programming", "Use Programming skill for the first time", false),
    FIRST_CHARISMA("First Charisma", "Use Charisma skill for the first time", false),
    FIRST_CREATIVITY("First Creativity", "Use Creativity skill for the first time", false),
    FIRST_LOGIC("First Logic", "Use Logic skill for the first time", false),
    FIRST_GARDENING("First Gardening", "Use Gardening skill for the first time", false),
    FIRST_MUSIC("First Music", "Use Music skill for the first time", false),
    FIRST_WRITING("First Writing", "Use Writing skill for the first time", false),
    FIRST_PAINTING("First Painting", "Use Painting skill for the first time", false),
    FRIENDLY("Friendly", "Become friends with every other character", true),
    EVIL("Evil", "Become enemies with every other character", true);

    private final String title;
    private final String description;
    private final boolean hidden;

    AchievementType(String title, String description, boolean hidden) {
        this.title = title;
        this.description = description;
        this.hidden = hidden;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHidden() {
        return hidden;
    }
}
