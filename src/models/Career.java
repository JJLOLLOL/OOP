package models;

public class Career {
    public  String title;
    public double salary;
    public double duration;
    public int progression;

    public Career(String title, double salary, double duration) {
        this.title = title;
        this.salary = salary;
        this.duration = duration;
        this.progression = 0;
    }

    public String getTitle() {
        return title;
    }

    public double getSalary() {
        return salary;
    }

    public int getProgression() {
        return progression;
    }

    public double getDuration() {
        return duration;
    }
}