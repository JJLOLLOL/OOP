package core;

import ui.GameState;
import ui.InitializationState;

public class GameEngine {
    private boolean isRunning;
    private GameState currentGameState;

    public GameEngine() {
        this.isRunning = false;
    }

    public void setGameState(GameState newState) {
        this.currentGameState = newState;
    }

    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }

    public void start() {
        if (!isRunning) {
            setIsRunning(true);
            // Trigger point to start UI, navigating between interfaces handled by UI classes
            setGameState(new InitializationState());
            while (isRunning) {
                currentGameState.render(this);
            }
        } else {
            System.out.println("Game engine failed to start!");
        }
    }

    public void end(){
        setIsRunning(false);
    }

//    private void displayHUD() {
//        System.out.println("\n------------------------------------------------");
//        System.out.println("Time: " + formatTime(gameTimeMinutes));
//        player.displayInfo();
//        System.out.println("------------------------------------------------");
//    }

//    private String formatTime(int totalMinutes) {
//        int day = (totalMinutes / (24 * 60)) + 1;
//        int hour = (totalMinutes % (24 * 60)) / 60;
//        int minute = totalMinutes % 60;
//        return String.format("Day %d, %02d:%02d", day, hour, minute);
//    }

//    private void displayMenu() {
//        System.out.println("1. Eat");
//        System.out.println("2. Sleep");
//        System.out.println("3. Work");
//        System.out.println("4. Socialize");
//        System.out.println("5. Do Nothing (Wait)");
//        System.out.println("6. Quit");
//        System.out.print("Choose an action: ");
//    }

//    private void handleInput() {
//        String choice = scanner.nextLine();
//        Activity activity = null;
//
//        switch (choice) {
//            case "1":
//                activity = new Activity("Eat", 30);
//                activity.addEffect("Hunger", 30.0);
////                activity.addEffect("Bladder", -5.0); // Eating slightly fulls bladder
//                break;
//            case "2":
//                activity = new Activity("Sleep", 480); // 8 hours
//                activity.addEffect("Energy", 80.0);
//                activity.addEffect("Hunger", -20.0);
//                break;
//            case "3":
//                activity = new Activity("Work", 240); // 4 hours
//                activity.addEffect("Money", 50.0);
//                activity.addEffect("Energy", -30.0);
//                activity.addEffect("Fun", -20.0);
//                break;
//            case "4":
//                System.out.println("Who do you want to talk to?");
//                for (int i = 0; i < npcs.size(); i++) {
//                    System.out.println((i + 1) + ". " + npcs.get(i).getName());
//                }
//                String npcChoiceStr = scanner.nextLine();
//                try {
//                    int npcIndex = Integer.parseInt(npcChoiceStr) - 1;
//                    if (npcIndex >= 0 && npcIndex < npcs.size()) {
//                        NPCCharacter npc = npcs.get(npcIndex);
//                        npc.interact(player, "Talk");
//                        activity = new Activity("Socialize", 30);
//                        activity.addEffect("Social", 25.0);
//                        activity.addEffect("Energy", -5.0);
//                    } else {
//                        System.out.println("Invalid NPC.");
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Invalid input.");
//                }
//                break;
//            case "5":
//                activity = new Activity("Wait", 30);
//                break;
//            case "6":
//                isRunning = false;
//                System.out.println("Goodbye!");
//                return;
//            default:
//                System.out.println("Invalid choice.");
//                return;
//        }
//
//        if (activity != null) {
//            player.performActivity(activity);
//            advanceTime(activity.getDurationMinutes());
//        }
//    }

//    private void advanceTime(int minutes) {
//        gameTimeMinutes += minutes;
//        player.update(minutes);
//        for (NPCCharacter npc : npcs) {
//            npc.update(minutes);
//        }
//
//        // Critical status check
//        if (player.getNeeds().get("Hunger").getValue() <= 0) {
//            System.out.println("WARNING: Your Sim is starving!");
//        }
//    }
}
