package ui.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.career.CareerList;
import ui.UITestSupport;

class StatsPanelViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void buildShowsCareerNeedsMoneyAndNearbyCharacters() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.player.joinCareer(CareerList.DOCTOR);
        fixture.player.initializeRelationshipWith(fixture.roommate);
        fixture.player.changeRelationshipWith(fixture.roommate, 40);
        fixture.player.initializeRelationshipWith(fixture.npc);
        fixture.player.changeRelationshipWith(fixture.npc, -30);

        List<String> lines = StatsPanelView.build(fixture.player, fixture.home, fixture.state, fixture.world);

        assertTrue(UITestSupport.plain(lines.get(0)).contains("Alex (25M)"));
        assertTrue(UITestSupport.plain(lines.get(1)).contains("Doctor"));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Hunger")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Social")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Money: $1000.00")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("At Home")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Jamie [Friendly] 40")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Taylor [Disliked] -30")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("Reads by the window.")));
    }

    @Test
    void buildShowsUnemployedAndNoOneNearbyWhenAlone() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.player.setLocation(fixture.cafe);
        fixture.roommate.setLocation(fixture.park);
        fixture.npc.setLocation(fixture.park);

        List<String> lines = StatsPanelView.build(fixture.player, fixture.cafe, fixture.state, fixture.world);

        assertEquals("Unemployed", UITestSupport.plain(lines.get(1)));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("At Cafe")));
        assertTrue(lines.stream().map(UITestSupport::plain).anyMatch(line -> line.contains("No one nearby.")));
    }
}
