package services;

import models.House;
import models.SimCharacter;

public class HouseService {

    private HouseService() {

    }

    public static boolean purchaseHouse(SimCharacter buyer, House house) {
        if (house.isOwned()) {
            return false; // House is already owned
        }
        if (buyer.getMoney() < house.getHousePrice()) {
            return false; // Not enough money to purchase
        }
        buyer.setMoney(house.getHousePrice());
        house.isOwned();

        return true;
    }

    public static String getPurchaseMessage(SimCharacter buyer, House house, boolean success) {
        if (!success) {
            if (house.isOwned()) {
                return "Sorry, this house is already owned.";
            } else {
                return "Insufficient funds! You need $" + house.getHousePrice() + " to purchase this house.";
            }
        }
        return buyer.getName() + " purchased " + house.getLocationName() + " for $" + house.getHousePrice() + "!";
    }

    public static boolean upgradeHouse(SimCharacter owner, House currentHouse, House nextHouse) {
        if (!currentHouse.isOwned()) {
            return false; // Current house is not owned
        }
        if (nextHouse.getHouseTier() != currentHouse.getHouseTier() + 1) {
            return false; // Can only upgrade to the next tier
        }

        double upgradeCost = nextHouse.getHousePrice() - currentHouse.getHousePrice();

        if (owner.getMoney() < upgradeCost) {
            return false;
        }

        // Process upgrade
        owner.setMoney(-upgradeCost);
        currentHouse.upgradeHouse(nextHouse);

        return true;

    }

}
