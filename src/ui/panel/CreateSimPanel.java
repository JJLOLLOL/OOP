package ui.panel;

import java.util.ArrayList;
import java.util.List;

public class CreateSimPanel implements Panel {

    private String name = "";
    private String age = "";
    private String gender = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();
        lines.add("Create Your Sim");
        lines.add("");
        lines.add("Name: " + name);
        lines.add("Age: " + age);
        lines.add("Gender: " + gender);
        return lines;
    }
}
