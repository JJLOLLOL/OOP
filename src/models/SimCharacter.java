package models;
import java.util.HashMap;
import java.util.Map;

public class SimCharacter extends Character {
    private Map<String, Need> needs = new HashMap<>();
//    private Map<String, Skill> skills;
    private double money;
//    private String career; // Placeholder for now

    public SimCharacter(String name, int age, String gender) {
        super(name, age, gender);
        this.money = 1000.0; // Starting money
//        this.career = "Unemployed";
//        this.skills = new HashMap<>();
        initializeNeeds();
//        initializeSkills();
    }

    @Override
    public void update(int minutesPassed) {

    }

//    private void initializeSkills() {
//        skills.put("Cooking", new Skill("Cooking"));
//        skills.put("Logic", new Skill("Logic"));
//        skills.put("Charisma", new Skill("Charisma"));
//    }

    private void initializeNeeds() {
        // Core gameplay mechanics: Needs
        needs.put("Hunger", new Need("Hunger", 5.0));
        needs.put("Energy", new Need("Energy", 3.0));
        needs.put("Hygiene", new Need("Hygiene", 4.0));
        needs.put("Fun", new Need("Fun", 6.0));
//        needs.put("Bladder", new Need("Bladder", 8.0));
        needs.put("Social", new Need("Social", 4.0));
    }

    public void performActivity(Activity activity) {
        System.out.println(name + " is doing " + activity.getName() + "...");

        Map<String, Double> effects = activity.getNeedEffects();
        for (Map.Entry<String, Double> entry : effects.entrySet()) {
            String needName = entry.getKey();
            Double amount = entry.getValue();

            if (needName.equals("Money")) {
                this.money += amount;
            } else if (needs.containsKey(needName)) {
                needs.get(needName).increase(amount);
            }
        }

        if (activity.getName().equals("Work")) {
            // Work improves Logic slightly?
            // Skills.get("Logic").practice(10.0);
        } else if (activity.getName().equals("Socialize")) {
            // skills.get("Charisma").practice(15.0);
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("=== " + name + " (" + agegroup + ") ===");
        System.out.println("Money: $" + String.format("%.2f", money));
//        System.out.println("Career: " + career);
        System.out.println("--- Needs ---");
        for (Need need : needs.values()) {
            System.out.println(need.toString());
        }
//        System.out.println("--- Skills ---");
//        for (Skill skill : skills.values()) {
//            System.out.println(skill.toString());
//        }
        System.out.println("==================");
    }

    public Map<String, Need> getNeeds() {
        return needs;
    }
}
