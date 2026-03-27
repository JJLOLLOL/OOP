package data;

import java.util.List;

import models.furniture.Furniture;
import models.location.House;

/**
 * A data-holding class for the game's shop inventory.
 * This object is constructed by the {@link data.parser.ShopParser} from external data files.
 */
public class ShopInventory {
    private final List<House> availableHouses;
    private final List<Furniture> availableFurniture;

    /**
     * Creates a shop inventory from pre-parsed houses and furniture items.
     *
     * @param houses the houses available for purchase
     * @param furniture the furniture items available for purchase
     */
    public ShopInventory(List<House> houses, List<Furniture> furniture) {
        this.availableHouses = houses;
        this.availableFurniture = furniture;
    }

    public List<House> getAvailableHouses() {
        return availableHouses;
    }

    public List<Furniture> getAvailableFurniture() {
        return availableFurniture;
    }
}
