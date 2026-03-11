package models.furnitureactions;

import java.util.Map;

// Factory class to create different types of furniture with their associated actions

// Code to create furniture in Main.java or in some initialization class:
/*
Furniture bed = FurnitureFactory.createBed();
Furniture hotplate = FurnitureFactory.createSingleHotplate();
*/

// To have Sim perform an action on a piece of furniture:
/*
Furniture bed = FurnitureFactory.createBed();
bed.performAction("Sleep", sim);
*/


// To store multiple pieces of furniture in a map for easy access in Location:
/* 
Map<String, Furniture> furniture = new HashMap<>();
furniture.put("Bed", FurnitureFactory.createBed());
furniture.put("Oven", FurnitureFactory.createOven());

// Then later:
furniture.get("Bed").performAction("Nap", sim);
*/

public class FurnitureFactory {

    public static Furniture createCheapMattress() {
        Furniture cheapMattress = new Furniture("Cheap Mattress", "A simple mattress for basic rest");
        cheapMattress.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 10.0, "Hunger", -10.0), // Need changes
                Map.of(), // Skill changes
                0.0, // Money cost
                1.0)); // Time required in hours
        cheapMattress.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 35.0, "Hunger", -40.0),
                Map.of(),
                0.0,
                8.0));
        return cheapMattress;
    }

    public static Furniture createSingleHotplate() {
        Furniture hotplate = new Furniture("Single Hotplate", "A simple hotplate for cooking");
        hotplate.addAction(new FurnitureAction(
                "Cook Quick Meal",
                "Prepare a simple meal.",
                Map.of("Hunger", 20.0, "Energy", -5.0),
                Map.of("Cooking", 8.0),
                5.0,
                0.5));
        hotplate.addAction(new FurnitureAction(
                "Bake Meal",
                "Cook a proper baked meal.",
                Map.of("Hunger", 30.0, "Energy", -8.0),
                Map.of("Cooking", 15.0),
                8.0,
                1.0));
        return hotplate;
    }
}
