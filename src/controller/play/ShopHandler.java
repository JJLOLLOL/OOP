package controller.play;

import controller.PlayController;
import core.ActionResult;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.location.House;
import services.NotificationService;
import ui.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles shop browsing, house purchasing, furniture purchasing, and furniture
 * selling flows.
 */
public class ShopHandler implements PlayInputHandler {

    private PlayController.Step internalStep = PlayController.Step.SHOP;
    private List<House> housesForSale;
    private List<Furniture> furnitureForSale;

    /**
     * Routes input between the shop's sub-menus.
     *
     * @param input the player's raw input
     * @param context the gameplay context
     * @return {@code true} when the menu changes
     */
    @Override
    public boolean handleInput(String input, PlayContext context) {
        return switch (internalStep) {
            case SHOP -> handleShopMenu(input, context);
            case SHOP_HOUSES -> handleShopHouses(input, context);
            case SHOP_FURNITURE -> handleShopFurniture(input, context);
            case SELL_FURNITURE -> handleSellFurniture(input, context);
            default -> false;
        };
    }

    /**
     * Handles the top-level shop menu and loads the appropriate inventory for
     * the chosen branch.
     */
    private boolean handleShopMenu(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        SimCharacter player = context.getActivePlayer();
        switch (input) {
            case "1" -> {
                this.housesForSale = context.getShopInventory().getAvailableHouses().stream().collect(Collectors.toList());
                if (housesForSale.isEmpty()) {
                    NotificationService.add(player, "No houses available for purchase.");
                    return false;
                }
                this.internalStep = PlayController.Step.SHOP_HOUSES;
            }
            case "2" -> {
                if (player.getCurrentHouse() == null) {
                    NotificationService.add(player, "You must own a house to purchase furniture! Buy a house first.");
                    return false;
                }
                this.furnitureForSale = context.getShopInventory().getAvailableFurniture();
                this.internalStep = PlayController.Step.SHOP_FURNITURE;
            }
            case "3" -> {
                if (player.getCurrentHouse() == null) {
                    NotificationService.add(player, "You must own a house to sell furniture!");
                    return false;
                }
                this.furnitureForSale = new ArrayList<>(player.getCurrentHouse().getFurnitureViews());
                if (furnitureForSale.isEmpty()) {
                    NotificationService.add(player, "Your house has no furniture to sell.");
                    return false;
                }
                this.internalStep = PlayController.Step.SELL_FURNITURE;
            }
            default -> {
                Renderer.showError("Enter 1, 2, 3, or 0 to go back.");
                return false;
            }
        }
        return true;
    }

    /**
     * Purchases the selected house and returns to the top-level shop menu.
     */
    private boolean handleShopHouses(String input, PlayContext context) {
        if (input.equals("0")) {
            this.internalStep = PlayController.Step.SHOP;
            return true;
        }
        return PlayController.pickFromList(input, housesForSale, idx -> {
            House house = housesForSale.get(idx);
            ActionResult result = context.getActivePlayer().purchaseHouse(house);
            NotificationService.add(context.getActivePlayer(), result.getMessage());
            this.internalStep = PlayController.Step.SHOP;
        });
    }

    /**
     * Purchases the selected furniture item and returns to the top-level shop
     * menu.
     */
    private boolean handleShopFurniture(String input, PlayContext context) {
        if (input.equals("0")) {
            this.internalStep = PlayController.Step.SHOP;
            return true;
        }
        return PlayController.pickFromList(input, furnitureForSale, idx -> {
            Furniture furniture = furnitureForSale.get(idx);
            ActionResult result = context.getActivePlayer().buyFurniture(furniture);
            NotificationService.add(context.getActivePlayer(), result.getMessage());
            this.internalStep = PlayController.Step.SHOP;
        });
    }

    /**
     * Sells the selected furniture item from the active sim's house.
     */
    private boolean handleSellFurniture(String input, PlayContext context) {
        if (input.equals("0")) {
            this.internalStep = PlayController.Step.SHOP;
            return true;
        }
        return PlayController.pickFromList(input, furnitureForSale, idx -> {
            Furniture furniture = furnitureForSale.get(idx);
            ActionResult result = context.getActivePlayer().sellFurniture(furniture);
            NotificationService.add(context.getActivePlayer(), result.getMessage());
            this.internalStep = PlayController.Step.SHOP;
        });
    }

    /**
     * Resets the shop handler back to its top-level menu.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        this.internalStep = PlayController.Step.SHOP;
        this.housesForSale = null;
        this.furnitureForSale = null;
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return the current shop sub-step
     */
    @Override
    public PlayController.Step getStep() {
        return internalStep;
    }

    // Accessors for view
    public List<House> getHousesForSale() { return housesForSale; }
    public List<Furniture> getFurnitureForSale() { return furnitureForSale; }
}
