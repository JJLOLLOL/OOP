package models;

public class NPCCharacter extends Character {
    private String relationshipStatus; // This is the relationships status: "Stranger", "Friend", etc.
    private int relationshipScore; // -100 to 100 (For Negative and Positive

    public NPCCharacter(String name, int age, String gender, String occupation) {
        super(name, age, gender);
        this.relationshipScore = 0;
        this.relationshipStatus = "Stranger"; // This is the default status
    }

    @Override
    public void update(int minutesPassed) {
        // NPC AI logic (move around, do random things)
        // For Phase 1, basic placeholder
        if (Math.random() < 0.1) {
            // Randomly change location or something
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("[NPC] " + name + " | Rel: " + relationshipStatus + " (" + relationshipScore + ")");
    }

    public void interact(SimCharacter sim, String interactionType) {
        // Simple interaction logic
        if (interactionType.equals("Talk")) {
            relationshipScore += 5;
            System.out.println(sim.getName() + " talked to " + name + ". Relationship improved.");
        } else if (interactionType.equals("Argue")) {
            relationshipScore -= 10;
            System.out.println(sim.getName() + " argued with " + name + ". Relationship worsened.");
        }
        updateRelationshipStatus();
    }

    private void updateRelationshipStatus() {
        if (relationshipScore > 50)
            relationshipStatus = "Friend";
        else if (relationshipScore > 0)
            relationshipStatus = "Acquaintance";
        else if (relationshipScore > -25)
            relationshipStatus = "Stranger"; // 0 to -25
        else if (relationshipScore > -100)
            relationshipStatus = "Enemy";
        else
            relationshipStatus = "Nemesis";
    }
}
