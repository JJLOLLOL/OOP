package models;

public class Career implements ProgressBar{
    public  String title;
    public double salary;
    public double duration; //in hours to represent duration the Sim is at work
    public double progress;
    public int level;
    public String role;

    public Career(String title, double salary, double duration, String role) {
        this.title = title;
        this.salary = salary;
        this.level = 1;
        this.duration = duration;
        this.progress = 0.0;
        this.role = role;
    }

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public double getSalary() {
        return salary;
    }

    public double getDuration() {
        return duration;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    //abstract methods from ProgressBar
    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public void addProgress(double amount){
        this.progress += amount;
        if (this.progress >= 100.0) {
            this.level++;
            this.progress -= 100.0;
            updateRole();
        }
    }

    private void updateRole() {
        if (this.level >= 7) {
            this.role = CareerRoles.EXECUTIVE;
        }
        else if (this.level == 6) {
            this.role = CareerRoles.DIRECTOR;
        }
        else if (this.level == 5){
            this.role = CareerRoles.MANAGER;
        }
        else if (this.level == 4){
            this.role = CareerRoles.SENIOR;
        }
        else if (this.level == 3){
            this.role = CareerRoles.EMPLOYEE;
        }
        else if (this.level == 2){
            this.role = CareerRoles.JUNIOR;
        }
        else {
            this.role = CareerRoles.INTERN;
        }
    }



}