package models.character.housing;

import models.location.House;
import models.character.finances.CharacterFinances;
import models.furniture.Furniture;

/**
 * Encapsulates house ownership, upgrades, and furniture transactions for a
 * sim.
 */
public class CharacterHousing {
    private House currentHouse;

    /**
     * Outcomes produced by house and furniture operations.
     */
    public enum HousingResult {
        SUCCESS,
        HOUSE_FULL,
        FURNITURE_NOT_FOUND,
        INSUFFICIENT_FUNDS
    }

    public boolean hasHouse() {
        return currentHouse != null;
    }

    public House getCurrentHouse() {
        return currentHouse;
    }

    /**
     * Assigns the current house for the sim.
     *
     * @param house the house to assign
     */
    public void assignHouse(House house) {
        if (house == null) {
            throw new IllegalArgumentException("House cannot be null.");
        }
        this.currentHouse = house;
    }

    /**
     * Attempts to buy and place a furniture item in the current house.
     *
     * @param furniture the furniture being purchased
     * @param finances the finances used to pay for the purchase
     * @return the outcome of the purchase attempt
     */
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

    /**
     * Sells a furniture item from the current house for a partial refund.
     *
     * @param furniture the furniture being sold
     * @param finances the finances receiving the refund
     * @return the outcome of the sale attempt
     */
    public HousingResult sellFurniture(Furniture furniture, CharacterFinances finances) {
        if (furniture == null) {
            throw new IllegalArgumentException("furniture cannot be null.");
        }
        if (!currentHouse.containsFurniture(furniture)) {
            return HousingResult.FURNITURE_NOT_FOUND;
        }

        currentHouse.removeFurniture(furniture);
        finances.earnMoney(furniture.getPrice() * 0.5);
        return HousingResult.SUCCESS;
    }

    /**
     * Upgrades the current house to the supplied target house definition.
     *
     * @param targetHouse the house definition to upgrade to
     * @param finances the finances used to pay for the upgrade
     * @return the outcome of the upgrade attempt
     */
    public HousingResult upgradeTo(House targetHouse, CharacterFinances finances) {
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
