package models.career;

import models.skill.SkillType;

/**
 * Represents the available career paths a Sim can choose from.
 * Each career defines a title, base salary, required working hours, and related skills.
 */
public enum CareerList {
    SOFTWARE_DEVELOPER("Software Developer", 225.0, 8.0, new SkillType[]{SkillType.PROGRAMMING, SkillType.LOGIC, SkillType.CREATIVITY}),
    ENGINEER("Engineer", 210.0, 8.0, new SkillType[]{SkillType.LOGIC, SkillType.PROGRAMMING}),
    DOCTOR("Doctor", 300.0, 10.0, new SkillType[]{SkillType.LOGIC, SkillType.CHARISMA}),
    TEACHER("Teacher", 175.0, 8.0, new SkillType[]{SkillType.CHARISMA, SkillType.CREATIVITY, SkillType.LOGIC}),
    LAWYER("Lawyer", 275.0, 9.0, new SkillType[]{SkillType.CHARISMA, SkillType.LOGIC}),
    POLICE_OFFICER("Police Officer", 160.0, 10.0, new SkillType[]{SkillType.FITNESS, SkillType.CHARISMA}),
    ACCOUNTANT("Accountant", 190.0, 8.0, new SkillType[]{SkillType.LOGIC}),
    BUSINESS_MANAGER("Business Manager", 250.0, 9.0, new SkillType[]{SkillType.CHARISMA, SkillType.LOGIC, SkillType.CREATIVITY}),
    CHEF("Chef", 150.0, 10.0, new SkillType[]{SkillType.COOKING, SkillType.CREATIVITY}),
    ARTIST("Artist", 125.0, 6.0, new SkillType[]{SkillType.CREATIVITY, SkillType.PAINTING}),
    MUSICIAN("Musician", 130.0, 6.0, new SkillType[]{SkillType.MUSIC, SkillType.CREATIVITY}),
    WRITER("Writer", 140.0, 7.0, new SkillType[]{SkillType.WRITING, SkillType.CREATIVITY}),
    JOBLESS("Jobless", 0.0, 0.0, new SkillType[]{});

    private final String title;
    private final double baseSalary;
    private final double workingHours;
    private final SkillType[] relatedSkills;

    /**
     * Constructs a new CareerList enum constant.
     *
     * @param title         the display title of the career
     * @param baseSalary    the base daily salary for the career
     * @param workingHours  the number of hours required for a shift
     * @param relatedSkills an array of skill names that can be improved while working
     */
    CareerList(String title, double baseSalary, double workingHours, SkillType[] relatedSkills) {
        this.title = title;
        this.baseSalary = baseSalary;
        this.workingHours = workingHours;
        this.relatedSkills = relatedSkills;
    }

    /**
     * Retrieves the display title of the career.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the base daily salary of the career.
     *
     * @return the base salary
     */
    public double getBaseSalary() {
        return baseSalary;
    }

    /**
     * Retrieves the number of working hours required for a shift in this career.
     *
     * @return the working hours
     */
    public double getWorkingHours() {
        return workingHours;
    }

    /**
     * Retrieves the list of skills associated with the career.
     *
     * @return an array of related skill names
     */
    public SkillType[] getRelatedSkills() {
        return relatedSkills;
    }
}
