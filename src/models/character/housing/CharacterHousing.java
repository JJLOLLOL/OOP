package models.character.housing;

import models.location.House;
import models.character.finances.CharacterFinances;
import models.furniture.Furniture;

public class CharacterHousing {
    private House currentHouse;

    public enum HousingResult {
        SUCCESS,
        HOUSE_FULL,
        HOUSE_EMPTY,
        INSUFFICIENT_FUNDS
    }
    
    public boolean hasHouse() {
        return currentHouse != null;
    }
    
    public House getCurrentHouse() {
        return currentHouse;
    }
    
    public void assignHouse(House house) {
        if (house == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        this.currentHouse = house;
    }
    
    public HousingResult buyFurniture(Furniture furniture, CharacterFinances finances) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        if (finances == null) {
            throw new IllegalArgumentException("Finances cannot be null.");
        }
        if (currentHouse == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        if (!currentHouse.canAddFurniture()) {
            return HousingResult.HOUSE_FULL;
        }
        if (!finances.canAfford(furniture.getPrice())) {
            return HousingResult.INSUFFICIENT_FUNDS;
        }
        currentHouse.addFurniture(furniture);
        finances.spendMoney(furniture.getPrice());
        return HousingResult.SUCCESS;
    }
    public HousingResult sellFurniture(Furniture furniture, CharacterFinances finances) {
        if (furniture == null) {
            throw new IllegalArgumentException("furniture cannot be null.");
        }
        if (!currentHouse.containsFurniture(furniture)) {
            return HousingResult.HOUSE_EMPTY;
        }

        currentHouse.removeFurniture(furniture);
        finances.earnMoney(furniture.getPrice() * 0.5);
        return HousingResult.SUCCESS;
    }
    
    public HousingResult buyHouse(House targetHouse, CharacterFinances finances) {
        if (targetHouse == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        if (finances == null) {
            throw new IllegalArgumentException("Finances cannot be null.");
        }
        if (currentHouse == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        if (!finances.canAfford(targetHouse.getPrice())) {
            return HousingResult.INSUFFICIENT_FUNDS;
        }
        currentHouse.upgradeTo(targetHouse);
        finances.spendMoney(targetHouse.getPrice());
        return HousingResult.SUCCESS;
    }
}