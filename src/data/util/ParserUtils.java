package data.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Shared parsing helpers for the game's plain-text data files.
 */
public class ParserUtils {

    /**
     * Generic block parser. Finds all blocks of a given type and adds their
     * properties to a list of maps.
     */
    public static void parseBlocks(List<String> lines, String blockMarker, List<Map<String, String>> propsList) {
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

    /**
     * Parses a string of effects (e.g., "ENERGY,10;HUNGER,-5") into a map.
     */
    public static <T extends Enum<T>> Map<T, Double> parseEffects(String effectsStr, Function<String, T> enumResolver) {
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
}
