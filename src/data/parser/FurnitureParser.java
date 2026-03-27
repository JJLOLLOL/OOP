package data.parser;

import data.util.FileUtils;
import data.util.ParserUtils;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.need.NeedType;
import models.skill.SkillType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnitureParser {

    public Map<String, Furniture> parse(String resourcePath) throws IOException {
        List<String> lines = FileUtils.readFile(resourcePath);
        List<Map<String, String>> furniturePropsList = new ArrayList<>();
        List<Map<String, String>> actionPropsList = new ArrayList<>();

        // First pass: categorize all properties into blocks
        ParserUtils.parseBlocks(lines, "[FURNITURE]", furniturePropsList);
        ParserUtils.parseBlocks(lines, "[ACTION]", actionPropsList);

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

        Map<NeedType, Double> needs = ParserUtils.parseEffects(props.get("NEEDS"), s -> NeedType.valueOf(s.toUpperCase()));
        Map<SkillType, Double> skills = ParserUtils.parseEffects(props.get("SKILLS"), s -> SkillType.valueOf(s.toUpperCase()));

        FurnitureAction action = new FurnitureAction(name, desc, needs, skills, cost, time);
        parent.addAction(action);
    }
}