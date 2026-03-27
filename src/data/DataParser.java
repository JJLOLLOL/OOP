package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import models.character.NPCCharacter;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.need.NeedType;
import models.skill.SkillType;
import types.Gender;
import models.location.House;
import models.location.Location;

/**
 * Parses game data (locations, NPCs) from text files.
 * This decouples the core game logic from the specific world data,
 * allowing data to be modified or replaced without changing the core engine.
 */
public class DataParser {

    /**
     * Loads all world data from the data files.
     *
     * @return A {@link WorldData} object containing all loaded locations and NPCs.
     * @throws RuntimeException if data files cannot be read.
     */
    public WorldData loadWorldData() {
        try {
            // 1. Parse all furniture and their actions
            Map<String, Furniture> furnitureMap = parseFurniture("data/furniture.txt");

            // Set the global work action for the SimCharacter class, removing the dependency on FurnitureFactory
            Furniture workDesk = furnitureMap.get("WorkDesk");
            if (workDesk != null && workDesk.getAction("Work") != null) {
                SimCharacter.setWorkAction(workDesk.getAction("Work"));
            } else {
                // This will cause a crash later if work() is called, which is intended to highlight the missing data.
                System.err.println("CRITICAL: Could not find 'Work' action for 'WorkDesk' in furniture.txt. The 'work' command will fail.");
            }

            // 2. Parse locations, using the furniture map
            Map<String, Location> locations = parseLocations("data/locations.txt", furnitureMap);
            // 3. Parse NPCs, using the locations map
            List<NPCCharacter> npcs = parseNPCs("data/npcs.txt", locations);
            // 4. Parse shop inventory, using the furniture map
            ShopInventory shopInventory = parseShopInventory("data/shop.txt", furnitureMap);
            return new WorldData(locations, npcs, shopInventory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load world data. Game cannot start.", e);
        }
    }

    /**
     * Parses the locations data file.
     */
    private Map<String, Location> parseLocations(String resourcePath, Map<String, Furniture> furnitureMap) throws IOException {
        Map<String, Location> locations = new HashMap<>();
        List<String> lines = readFile(resourcePath);

        Map<String, String> currentProperties = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            if (line.equals("[LOCATION]")) {
                if (!currentProperties.isEmpty()) {
                    Location loc = buildLocation(currentProperties, furnitureMap);
                    locations.put(loc.getLocationName(), loc);
                }
                currentProperties.clear();
            } else {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    currentProperties.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        // Build the last location
        if (!currentProperties.isEmpty()) {
            Location loc = buildLocation(currentProperties, furnitureMap);
            locations.put(loc.getLocationName(), loc);
        }

        return locations;
    }

    /**
     * Builds a Location or House object from a map of properties.
     */
    private Location buildLocation(Map<String, String> properties, Map<String, Furniture> furnitureMap) {
        String name = properties.get("NAME");
        String type = properties.get("TYPE");
        String furnitureStr = properties.getOrDefault("FURNITURE", "");

        ArrayList<Furniture> furnitureList = new ArrayList<>();
        if (!furnitureStr.isEmpty()) {
            furnitureList = java.util.Arrays.stream(furnitureStr.split(","))
                    .map(String::trim)
                    .map(furnitureMap::get)
                    .filter(java.util.Objects::nonNull) // Filter out any null furniture (e.g., if name not found)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if ("House".equals(type)) {
            return new House(name, furnitureList);
        } else {
            return new Location(name, furnitureList);
        }
    }

    /**
     * Parses the NPCs data file.
     */
    private List<NPCCharacter> parseNPCs(String resourcePath, Map<String, Location> locations) throws IOException {
        List<NPCCharacter> npcs = new ArrayList<>();
        List<String> lines = readFile(resourcePath);

        Map<String, String> currentProperties = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            if (line.equals("[NPC]")) {
                if (!currentProperties.isEmpty()) {
                    npcs.add(buildNpc(currentProperties, locations));
                }
                currentProperties.clear();
            } else {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    currentProperties.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        // Build the last NPC
        if (!currentProperties.isEmpty()) {
            npcs.add(buildNpc(currentProperties, locations));
        }

        return npcs;
    }

    /**
     * Builds an NPCCharacter object from a map of properties.
     */
    private NPCCharacter buildNpc(Map<String, String> properties, Map<String, Location> locations) {
        String name = properties.get("NAME");
        int age = Integer.parseInt(properties.get("AGE"));
        Gender gender = Gender.fromDataValue(properties.get("GENDER"));
        String desc = properties.get("DESC");
        String scheduleStr = properties.get("SCHEDULE");

        TreeMap<Integer, Location> schedule = new TreeMap<>();
        if (scheduleStr != null && !scheduleStr.isEmpty()) {
            for (String pair : scheduleStr.split(";")) {
                String[] parts = pair.split(",", 2);
                int time = Integer.parseInt(parts[0].trim());
                String locName = parts[1].trim();
                Location loc = locations.get(locName);
                if (loc != null) {
                    schedule.put(time, loc);
                }
            }
        }

        return new NPCCharacter(name, age, gender, desc, schedule);
    }

    /**
     * Parses furniture and actions from their data file.
     * This uses a two-pass approach over a list of property maps to ensure
     * actions can be attached to their parent furniture regardless of file order.
     */
    private Map<String, Furniture> parseFurniture(String resourcePath) throws IOException {
        List<String> lines = readFile(resourcePath);
        List<Map<String, String>> furniturePropsList = new ArrayList<>();
        List<Map<String, String>> actionPropsList = new ArrayList<>();

        // First pass: categorize all properties into blocks
        parseBlocks(lines, "[FURNITURE]", furniturePropsList);
        parseBlocks(lines, "[ACTION]", actionPropsList);

        // Second pass: build objects
        Map<String, Furniture> furnitureMap = new HashMap<>();
        for (Map<String, String> props : furniturePropsList) {
            Furniture f = buildFurniture(props);
            // Add by both original name and space-less name for robust lookup
            furnitureMap.put(f.getName(), f); // Add original name as key
            furnitureMap.put(f.getName().replaceAll("\\s+", ""), f); // Add space-less name as key
        }

        for (Map<String, String> props : actionPropsList) {
            buildAndAttachAction(props, furnitureMap);
        }

        return furnitureMap;
    }

    /**
     * Parses a shop inventory file.
     */
    private ShopInventory parseShopInventory(String resourcePath, Map<String, Furniture> allFurniture) throws IOException {
        List<String> lines = readFile(resourcePath);
        List<Map<String, String>> availableFurnitureProps = new ArrayList<>();
        List<Map<String, String>> houseForSaleProps = new ArrayList<>();

        parseBlocks(lines, "[AVAILABLE_FURNITURE]", availableFurnitureProps);
        parseBlocks(lines, "[HOUSE_FOR_SALE]", houseForSaleProps);

        List<Furniture> shopFurniture = new ArrayList<>();
        if (!availableFurnitureProps.isEmpty()) {
            String namesStr = availableFurnitureProps.get(0).get("NAMES");
            if (namesStr != null) {
                for (String name : namesStr.split(",")) {
                    Furniture f = allFurniture.get(name.trim());
                    if (f != null) {
                        shopFurniture.add(f);
                    }
                }
            }
        }

        List<House> shopHouses = new ArrayList<>();
        for (Map<String, String> props : houseForSaleProps) {
            shopHouses.add(buildShopHouse(props, allFurniture));
        }

        return new ShopInventory(shopHouses, shopFurniture);
    }

    /**
     * Generic block parser. Finds all blocks of a given type and adds their
     * properties to a list of maps.
     */
    private void parseBlocks(List<String> lines, String blockMarker, List<Map<String, String>> propsList) {
        Map<String, String> currentProperties = null;
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            // Any line starting with '[' is a block marker and signifies the end of the previous block.
            if (line.startsWith("[")) {
                if (currentProperties != null) {
                    propsList.add(currentProperties); // Finalize the previous block
                }
                // If this is the type of block we are looking for, start a new one.
                if (line.equals(blockMarker)) {
                    currentProperties = new HashMap<>();
                } else {
                    currentProperties = null; // Otherwise, we are not in a target block.
                }
            } else if (currentProperties != null && line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    currentProperties.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        if (currentProperties != null && !currentProperties.isEmpty()) {
            propsList.add(currentProperties);
        }
    }

    private Furniture buildFurniture(Map<String, String> props) {
        String name = props.get("NAME");
        String desc = props.get("DESC");
        double price = Double.parseDouble(props.get("PRICE"));
        return new Furniture(name, desc, price);
    }

    private void buildAndAttachAction(Map<String, String> props, Map<String, Furniture> furnitureMap) {
        String furnitureName = props.get("FURNITURE");
        Furniture parent = furnitureMap.get(furnitureName);
        if (parent == null) {
            System.err.println("Warning: Action defined for non-existent furniture: " + furnitureName);
            return;
        }

        String name = props.get("NAME");
        String desc = props.get("DESC");
        double cost = Double.parseDouble(props.get("COST"));
        double time = Double.parseDouble(props.get("TIME"));

        Map<NeedType, Double> needs = parseEffects(props.get("NEEDS"), s -> NeedType.valueOf(s.toUpperCase()));
        Map<SkillType, Double> skills = parseEffects(props.get("SKILLS"), s -> SkillType.valueOf(s.toUpperCase()));

        FurnitureAction action = new FurnitureAction(name, desc, needs, skills, cost, time);
        parent.addAction(action);
    }

    private House buildShopHouse(Map<String, String> props, Map<String, Furniture> allFurniture) {
        String name = props.get("NAME");
        double price = Double.parseDouble(props.get("PRICE"));
        double rate = Double.parseDouble(props.get("RATE"));
        int tier = Integer.parseInt(props.get("TIER"));
        String furnitureStr = props.getOrDefault("FURNITURE", "");

        ArrayList<Furniture> furnitureList = new ArrayList<>();
        if (!furnitureStr.isEmpty()) {
            furnitureList = java.util.Arrays.stream(furnitureStr.split(","))
                    .map(String::trim)
                    .map(allFurniture::get)
                    .filter(java.util.Objects::nonNull) // Filter out any null furniture (e.g., if name not found)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new House(name, furnitureList, price, rate, tier);
    }

    /**
     * Parses a string of effects (e.g., "ENERGY,10;HUNGER,-5") into a map.
     */
    private <T extends Enum<T>> Map<T, Double> parseEffects(String effectsStr, Function<String, T> enumResolver) {
        Map<T, Double> map = new HashMap<>();
        if (effectsStr == null || effectsStr.isBlank()) {
            return map;
        }

        for (String pair : effectsStr.split(";")) {
            String[] parts = pair.split(",");
            if (parts.length == 2) {
                try {
                    T type = enumResolver.apply(parts[0].trim());
                    double value = Double.parseDouble(parts[1].trim());
                    map.put(type, value);
                } catch (IllegalArgumentException e) {
                    System.err.println("Warning: Invalid enum constant in data file: " + parts[0].trim());
                }
            }
        }
        return map;
    }

    /**
     * Reads a resource file from the classpath into a list of strings.
     */
    private List<String> readFile(String resourcePath) throws IOException {
        List<String> lines = new ArrayList<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines;
        }

        Path fallbackPath = Path.of("src").resolve(resourcePath);
        if (!Files.exists(fallbackPath)) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(fallbackPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
