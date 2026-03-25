package models.skill;

public enum SkillType {
    COOKING("Cooking"),
    FITNESS("Fitness"),
    PROGRAMMING("Programming"),
    CHARISMA("Charisma"),
    CREATIVITY("Creativity"),
    LOGIC("Logic"),
    MUSIC("Music"),
    WRITING("Writing"),
    PAINTING("Painting");

    private final String name;

    SkillType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}