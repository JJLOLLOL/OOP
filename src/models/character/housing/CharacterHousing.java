package models.character.housing;

import models.location.House;
import models.actions.Furniture;
import models.character.finances.CharacterFinances;

public class CharacterHousing {
    private House currentHouse;
    
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
    
    public void purchaseFurniture(Furniture furniture, CharacterFinances finances) {
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
            throw new IllegalStateException("Furniture capacity reached.");
        }
        if (!finances.canAfford(furniture.getPrice())) {
            throw new IllegalStateException("Not enough money.");
        }
        currentHouse.addFurniture(furniture);
        finances.spendMoney(furniture.getPrice());
    }
    
    public void purchaseHouse(House targetHouse, CharacterFinances finances) {
        if (targetHouse == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        if (finances == null) {
            throw new IllegalArgumentException("Finances cannot be null.");
        }
        if (currentHouse == null) {
            throw new IllegalStateException("Sim must already have a current house.");
        }
        if (!finances.canAfford(targetHouse.getPrice())) {
            throw new IllegalStateException("Insufficient funds.");
        }
        
        currentHouse.upgradeTo(targetHouse);
        finances.spendMoney(targetHouse.getPrice());
    }
}