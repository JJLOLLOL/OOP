package models;

public enum AchievementType {
    // Career-based achievements
    FIRST_JOB("First Job", "Get your first non-jobless career", false),
    TECH_TRAILBLAZER("Tech Trailblazer", "Start a career in Software Developer or Engineer", false),
    HEALING_HANDS("Healing Hands", "Start a career as a Doctor", false),
    CREATIVE_SOUL("Creative Soul", "Start a creative career as an Artist, Musician, or Writer", false),
    PUBLIC_SERVICE("Public Service", "Start a career as a Teacher or Police Officer", false),
    BUSINESS_MINDED("Business Minded", "Start a career as a Lawyer, Accountant, or Business Manager", false),
    FIRST_PROMOTION("First Promotion", "Reach career rank 2", false),
    SENIOR_STAFF("Senior Staff", "Reach career rank 4", false),
    CORPORATE_EXECUTIVE("Corporate Executive", "Reach career rank 7", false),

    // Skill-based achievements
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

    // Social achievements
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
