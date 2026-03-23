package Types;

/**
 * Represents the various achievements a Sim can unlock during gameplay.
 * Achievements are categorized into career, skill, and social milestones.
 */
public enum AchievementList {

    // Career achievements
    FIRST_JOB("First Job", "Get your first non-jobless career"),
    TECH_TRAILBLAZER("Tech Trailblazer", "Start a career in Software Developer or Engineer"),
    HEALING_HANDS("Healing Hands", "Start a career as a Doctor"),
    CREATIVE_SOUL("Creative Soul", "Start a creative career as an Artist, Musician, or Writer"),
    PUBLIC_SERVICE("Public Service", "Start a career as a Teacher or Police Officer"),
    BUSINESS_MINDED("Business Minded", "Start a career as a Lawyer, Accountant, or Business Manager"),
    FIRST_PROMOTION("First Promotion", "Reach career rank 2"),
    SENIOR_STAFF("Senior Staff", "Reach career rank 4"),
    CORPORATE_EXECUTIVE("Corporate Executive", "Reach career rank 7"),
    // Skill achievements
    FIRST_COOKING("First Cooking", "Use Cooking skill for the first time"),
    FIRST_FITNESS("First Fitness", "Use Fitness skill for the first time"),
    FIRST_PROGRAMMING("First Programming", "Use Programming skill for the first time"),
    FIRST_CHARISMA("First Charisma", "Use Charisma skill for the first time"),
    FIRST_CREATIVITY("First Creativity", "Use Creativity skill for the first time"),
    FIRST_LOGIC("First Logic", "Use Logic skill for the first time"),
    FIRST_GARDENING("First Gardening", "Use Gardening skill for the first time"),
    FIRST_MUSIC("First Music", "Use Music skill for the first time"),
    FIRST_WRITING("First Writing", "Use Writing skill for the first time"),
    FIRST_PAINTING("First Painting", "Use Painting skill for the first time"),
    // Social achievements
    FRIENDLY("Friendly", "Become friends with every other character"),
    EVIL("Evil", "Become enemies with every other character");

    private final String title;
    private final String description;

    /**
     * Constructs a new AchievementList enum constant.
     *
     * @param title       the display name of the achievement
     * @param description a brief description of how to unlock the achievement
     */
    AchievementList(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Retrieves the display title of the achievement.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the description of how to unlock the achievement.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
