package data.parser;

import data.ShopInventory;
import data.util.FileUtils;
import data.util.ParserUtils;
import models.furniture.Furniture;
import models.location.House;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopParser {

    public ShopInventory parse(String resourcePath, Map<String, Furniture> allFurniture) throws IOException {
        List<String> lines = FileUtils.readFile(resourcePath);
        List<Map<String, String>> availableFurnitureProps = new ArrayList<>();
        List<Map<String, String>> houseForSaleProps = new ArrayList<>();

        ParserUtils.parseBlocks(lines, "[AVAILABLE_FURNITURE]", availableFurnitureProps);
        ParserUtils.parseBlocks(lines, "[HOUSE_FOR_SALE]", houseForSaleProps);

        List<Furniture> shopFurniture = new ArrayList<>();
        if (!availableFurnitureProps.isEmpty()) {
            String namesStr = availableFurnitureProps.get(0).get("NAMES");
            if (namesStr != null) {
                for (String name : namesStr.split(",")) {
                    Furniture f = allFurniture.get(name.trim());
                    if (f != null) {
                        shopFurniture.add(f);
                    } else {
                        System.err.println("Warning: Shop inventory lists unknown furniture: " + name.trim());
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
}