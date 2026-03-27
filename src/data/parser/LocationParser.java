package data.parser;

import data.util.FileUtils;
import models.furniture.Furniture;
import models.location.House;
import models.location.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parses location definitions from the locations data file.
 */
public class LocationParser {

    /**
     * Reads the supplied location resource and builds the corresponding
     * location objects.
     *
     * @param resourcePath the classpath-relative data file to parse
     * @param furnitureMap furniture definitions available for location lookup
     * @return the parsed locations keyed by display name
     * @throws IOException when the data file cannot be read
     */
    public Map<String, Location> parse(String resourcePath, Map<String, Furniture> furnitureMap) throws IOException {
        Map<String, Location> locations = new HashMap<>();
        List<String> lines = FileUtils.readFile(resourcePath);

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
     * Builds either a {@link House} or a plain {@link Location} from one parsed
     * location block.
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
}
