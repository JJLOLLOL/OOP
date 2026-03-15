package models;

public enum CareerList {
  SOFTWARE_DEVELOPER("Software Developer", 4500.0, 8.0,  new String[]{"Programming", "Logic", "Creativity"}),
  ENGINEER          ("Engineer",           4200.0, 8.0,  new String[]{"Logic", "Programming"}),
  DOCTOR            ("Doctor",             6000.0, 10.0, new String[]{"Logic", "Charisma"}),
  TEACHER           ("Teacher",            3500.0, 8.0,  new String[]{"Charisma", "Creativity", "Logic"}),
  LAWYER            ("Lawyer",             5500.0, 9.0,  new String[]{"Charisma", "Logic"}),
  POLICE_OFFICER    ("Police Officer",     3200.0, 10.0, new String[]{"Fitness", "Charisma"}),
  ACCOUNTANT        ("Accountant",         3800.0, 8.0,  new String[]{"Logic"}),
  BUSINESS_MANAGER  ("Business Manager",   5000.0, 9.0,  new String[]{"Charisma", "Logic", "Creativity"}),
  CHEF              ("Chef",               3000.0, 10.0, new String[]{"Cooking", "Creativity"}),
  ARTIST            ("Artist",             2500.0, 6.0,  new String[]{"Creativity", "Painting"}),
  MUSICIAN          ("Musician",           2600.0, 6.0,  new String[]{"Music", "Creativity"}),
  WRITER            ("Writer",             2800.0, 7.0,  new String[]{"Writing", "Creativity"}),
  JOBLESS           ("Jobless",            0.0,    0.0,  new String[]{});

  private final String title;
  private final double baseSalary;
  private final double workingHours;
  private final String[] relatedSkills;   // ← add this

  CareerList(String title, double baseSalary, double workingHours, String[] relatedSkills) {
    this.title         = title;
    this.baseSalary    = baseSalary;
    this.workingHours  = workingHours;
    this.relatedSkills = relatedSkills;
  }

  public String getTitle()          { return title; }
  public double getBaseSalary()     { return baseSalary; }
  public double getWorkingHours()   { return workingHours; }
  public String[] getRelatedSkills(){ return relatedSkills; }
}