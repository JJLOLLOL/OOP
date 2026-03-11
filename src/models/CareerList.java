package models;

public enum CareerList {
  SOFTWARE_DEVELOPER("Software Developer", 4500.0, 8.0),
  ENGINEER("Engineer", 4200.0, 8.0),
  DOCTOR("Doctor", 6000.0, 10.0),
  TEACHER("Teacher", 3500.0, 8.0),
  LAWYER("Lawyer",5500.0,9.0),
  POLICE_OFFICER("Police Officer",3200.0,10.0),
  ACCOUNTANT("Accountant",3800.0,8.0),
  BUSINESS_MANAGER("Business Manager", 5000.0, 9.0),
  CHEF("Chef", 3000.0, 10.0),
  ARTIST("Artist", 2500.0, 6.0),
  MUSICIAN("Musician", 2600.0, 6.0),
  WRITER("Writer", 2800.0, 7.0),
  JOBLESS("Jobless", 0.0, 0.0);

  private final String title;
  private final double baseSalary;
  private final double workingHours;

  CareerList(String title, double baseSalary, double workingHours) {
    this.title = title;
    this.baseSalary = baseSalary;
    this.workingHours = workingHours;
  }

  public String getTitle() {
    return title;
  }
  public double getBaseSalary() {
    return baseSalary;
  }
  public double getWorkingHours() {
    return workingHours;
  }
}