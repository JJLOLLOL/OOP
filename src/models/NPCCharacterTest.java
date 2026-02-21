package models;

public class NPCCharacterTest {
    public static void main(String[] args) {
        System.out.println("=== NPC Character Testing ===\n");
        
        // Test 1: Create an NPC doctor
        System.out.println("Test 1: Creating NPC Doctor");
        NPCCharacter doctor = new NPCCharacter("Dr. Smith", 45, "Male");
        doctor.displayInfo();
        System.out.println("Location: " + doctor.getLocation() + "\n");
        
        // Test 2: Schedule activities
        System.out.println("Test 2: Scheduling Doctor Activities");
        Activity work = new Activity("Work at Hospital", 480);
        work.addEffect("Money", 100.0);
        
        Activity sleep = new Activity("Sleep", 600);
        sleep.addEffect("Energy", 50.0);
        
        // Schedule work from 9 AM to 5 PM
        for (int hour = 9; hour < 17; hour++) {
            doctor.scheduleActivity(hour, work);
        }
        System.out.println("✓ Scheduled work 9 AM - 5 PM");
        
        // Schedule sleep from 10 PM to 8 AM
        for (int hour = 22; hour < 24; hour++) {
            doctor.scheduleActivity(hour, sleep);
        }
        for (int hour = 0; hour < 8; hour++) {
            doctor.scheduleActivity(hour, sleep);
        }
        System.out.println("✓ Scheduled sleep 10 PM - 8 AM\n");
        
        // Test 3: Update at different times
        System.out.println("Test 3: Testing Update at Different Times");
        System.out.println("\n--- 3 AM (sleeping time) ---");
        doctor.update(3 * 60); // 3 AM
        System.out.println("Location: " + doctor.getLocation());
        
        System.out.println("\n--- 12 PM (working time) ---");
        doctor.update(12 * 60); // 12 PM
        System.out.println("Location: " + doctor.getLocation());
        
        System.out.println("\n--- 11 PM (sleeping time) ---");
        doctor.update(23 * 60); // 11 PM
        System.out.println("Location: " + doctor.getLocation() + "\n");
        
        // Test 4: Interaction and reaction
        System.out.println("Test 4: Testing Interactions");
        SimCharacter player = new SimCharacter("Player", 25, "Female");
        System.out.println("\n--- Talking to doctor ---");
        doctor.interact(player, "Talk");
        doctor.displayInfo();
        
        System.out.println("\n--- Arguing with doctor ---");
        doctor.interact(player, "Argue");
        doctor.displayInfo();
        
        // Test 5: Relationship decay
        System.out.println("Test 5: Testing Relationship Decay");
        System.out.println("Initial relationship score after interactions:");
        doctor.displayInfo();
        
        System.out.println("\nAfter decay (simulating time passing):");
        for (int i = 0; i < 3; i++) {
            doctor.decayRelationships();
        }
        doctor.displayInfo();
        
        System.out.println("\n=== All Tests Complete ===");
    }
}
