package data.parser;

import data.util.FileUtils;
import models.character.NPCCharacter;
import models.location.Location;
import types.Gender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NpcParser {

    public List<NPCCharacter> parse(String resourcePath, Map<String, Location> locations) throws IOException {
        List<NPCCharacter> npcs = new ArrayList<>();
        List<String> lines = FileUtils.readFile(resourcePath);

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
                } else {
                    System.err.println("Warning: NPC '" + name + "' has a schedule with an unknown location: " + locName);
                }
            }
        }

        if (schedule.isEmpty()) {
            throw new IllegalStateException("NPC '" + name + "' has no valid schedule entries. Cannot determine starting location.");
        }

        return new NPCCharacter(name, age, gender, desc, schedule);
    }
}