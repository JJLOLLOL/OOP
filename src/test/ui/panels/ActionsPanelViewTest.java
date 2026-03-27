package ui.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.PlayController;
import types.RelationshipType;
import ui.UITestSupport;

class ActionsPanelViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void buildMainMenuShowsPrimaryActions() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        List<String> lines = ActionsPanelView.build(PlayController.Step.MAIN, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertEquals("Actions", UITestSupport.plain(lines.get(0)));
        assertEquals("1. Interact Objects", UITestSupport.plain(lines.get(1)));
        assertEquals("6. Exit Game", UITestSupport.plain(lines.get(6)));
    }

    @Test
    void buildInteractablesListsFurnitureAndBackItem() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        List<String> lines = ActionsPanelView.build(PlayController.Step.INTERACTABLES, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertEquals("Interact Objects", UITestSupport.plain(lines.get(0)));
        assertEquals("1. Study Desk", UITestSupport.plain(lines.get(1)));
        assertEquals("2. Arcade Machine", UITestSupport.plain(lines.get(2)));
        assertEquals("0. Back", UITestSupport.plain(lines.get(3)));
    }

        @Test
    void buildInteractableActionShowsSortedActionsAndEffects() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.playController.handleInput("1", fixture.state, fixture.world);
        fixture.playController.handleInput("1", fixture.state, fixture.world);

        List<String> lines = ActionsPanelView.build(
                fixture.playController.getActiveHandler().getStep(),
                fixture.home,
                fixture.player,
                fixture.state,
                fixture.world,
                fixture.playController);

        assertEquals("Study Desk", UITestSupport.plain(lines.get(0)));
        assertEquals("1. Code", UITestSupport.plain(lines.get(1)));
        assertEquals("2. Write", UITestSupport.plain(UITestSupport.findLineContaining(lines, "2. Write")));
        assertTrue(UITestSupport.findLineContaining(lines, "needs: -8 ENERGY").contains(ui.ConsoleUtils.BRIGHT_RED));
        assertTrue(UITestSupport.findLineContaining(lines, "+20xp PROGRAMMING").contains(ui.ConsoleUtils.BRIGHT_CYAN));
        assertTrue(UITestSupport.findLineContaining(lines, "time: 2h").contains(ui.ConsoleUtils.BRIGHT_WHITE));
        assertTrue(UITestSupport.findLineContaining(lines, "cost: $12").contains(ui.ConsoleUtils.BRIGHT_YELLOW));
    }

    @Test
    void buildSocialiseShowsRelationshipLabelsForCharactersAtLocation() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.player.initializeRelationshipWith(fixture.roommate);
        fixture.player.changeRelationshipWith(fixture.roommate, 30);

        List<String> lines = ActionsPanelView.build(PlayController.Step.SOCIALISE, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertEquals("Socialise", UITestSupport.plain(lines.get(0)));
        assertTrue(UITestSupport.findLineContaining(lines, "Jamie [Friendly]").contains(RelationshipType.FRIENDLY.label));
        assertTrue(UITestSupport.findLineContaining(lines, "Taylor [Acquaintance]").contains(RelationshipType.ACQUAINTANCE.label));
        assertEquals("0. Back", UITestSupport.plain(lines.get(lines.size() - 1)));
    }

    @Test
    void buildSocialiseActionListsAllInteractionOptions() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.playController.handleInput("2", fixture.state, fixture.world);
        fixture.playController.handleInput("1", fixture.state, fixture.world);

        List<String> lines = ActionsPanelView.build(
                fixture.playController.getActiveHandler().getStep(),
                fixture.home,
                fixture.player,
                fixture.state,
                fixture.world,
                fixture.playController);

        assertEquals("Interact: Jamie", UITestSupport.plain(lines.get(0)));
        assertEquals("1. Talk", UITestSupport.plain(lines.get(1)));
        assertEquals("4. Insult", UITestSupport.plain(lines.get(4)));
        assertEquals("0. Back", UITestSupport.plain(lines.get(5)));
    }

    @Test
    void buildChangeLocationMarksCurrentLocation() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
    
        List<String> lines = ActionsPanelView.build(
                PlayController.Step.CHANGE_LOCATION,
                fixture.home,
                fixture.player,
                fixture.state,
                fixture.world,
                fixture.playController);
    
        assertEquals("Go to...", UITestSupport.plain(lines.get(0)));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Home ← here")));
        assertTrue(UITestSupport.findLineContaining(lines, "Park").contains("Park"));
        assertTrue(UITestSupport.findLineContaining(lines, "Cafe").contains("Cafe"));
        assertEquals("0. Back", UITestSupport.plain(lines.get(lines.size() - 1)));
    }

    @Test
    void buildSwitchCharacterMarksActiveSim() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        List<String> lines = ActionsPanelView.build(PlayController.Step.SWITCH_CHARACTER, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertTrue(UITestSupport.plain(lines.get(1)).contains("Alex ← active"));
        assertTrue(UITestSupport.plain(lines.get(2)).contains("Jamie"));
    }

    @Test
    void buildPickCareerShowsCareerTableWithoutJoblessEntry() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        List<String> lines = ActionsPanelView.build(PlayController.Step.PICK_CAREER, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertEquals("Choose Career", UITestSupport.plain(lines.get(0)));
        assertTrue(UITestSupport.findLineContaining(lines, "Software Developer").contains("Software Developer"));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Programming")));
        assertFalse(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Jobless")));
    }

    @Test
    void buildShopAndHouseMenusUseShopInventory() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.playController.handleInput("5", fixture.state, fixture.world);
        fixture.playController.handleInput("1", fixture.state, fixture.world);

        List<String> shopLines = ActionsPanelView.build(PlayController.Step.SHOP, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);
        List<String> houseLines = ActionsPanelView.build(PlayController.Step.SHOP_HOUSES, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        assertEquals("Shop", UITestSupport.plain(shopLines.get(0)));
        assertEquals("1. Browse Houses", UITestSupport.plain(shopLines.get(1)));
        assertEquals("Houses for Sale", UITestSupport.plain(houseLines.get(0)));
        assertTrue(UITestSupport.plain(houseLines.get(1)).contains("Villa (Tier 3) - $2500"));
    }

    @Test
    void buildShopFurnitureAndSellFurnitureShowPricesAndRefunds() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.playController.handleInput("5", fixture.state, fixture.world);
        fixture.playController.handleInput("2", fixture.state, fixture.world);
        List<String> buyLines = ActionsPanelView.build(PlayController.Step.SHOP_FURNITURE, fixture.home,
                fixture.player, fixture.state, fixture.world, fixture.playController);

        UITestSupport.Fixture sellFixture = UITestSupport.fixture();
        sellFixture.playController.handleInput("5", sellFixture.state, sellFixture.world);
        sellFixture.playController.handleInput("3", sellFixture.state, sellFixture.world);
        List<String> sellLines = ActionsPanelView.build(PlayController.Step.SELL_FURNITURE, sellFixture.home,
                sellFixture.player, sellFixture.state, sellFixture.world, sellFixture.playController);

        assertEquals("Furniture for Sale", UITestSupport.plain(buyLines.get(0)));
        assertTrue(UITestSupport.plain(buyLines.get(1)).contains("Lamp - $80"));
        assertEquals("Sell Furniture from Your House", UITestSupport.plain(sellLines.get(0)));
        assertTrue(UITestSupport.plain(sellLines.get(1)).contains("Study Desk - Refund: $125.00"));
        assertTrue(UITestSupport.plain(sellLines.get(2)).contains("Arcade Machine - Refund: $200.00"));
    }
}
