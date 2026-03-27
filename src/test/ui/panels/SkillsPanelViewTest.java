package ui.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.skill.SkillType;
import ui.ConsoleUtils;
import ui.UITestSupport;

class SkillsPanelViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void buildShowsAllSkillsAndUsesThresholdColours() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.player.adjustSkillXp(SkillType.COOKING, 20);
        fixture.player.adjustSkillXp(SkillType.FITNESS, 50);
        fixture.player.adjustSkillXp(SkillType.PROGRAMMING, 80);

        List<String> lines = SkillsPanelView.build(fixture.player);
        String cooking = UITestSupport.findLineContaining(lines, "Cooking");
        String fitness = UITestSupport.findLineContaining(lines, "Fitness");
        String programming = UITestSupport.findLineContaining(lines, "Programming");

        assertEquals("Skills", UITestSupport.plain(lines.get(0)));
        assertEquals(1 + SkillType.values().length, lines.size());
        assertTrue(cooking.contains(ConsoleUtils.BRIGHT_BLUE));
        assertTrue(fitness.contains(ConsoleUtils.BRIGHT_YELLOW));
        assertTrue(programming.contains(ConsoleUtils.BRIGHT_GREEN));
        assertTrue(UITestSupport.plain(programming).contains("Lv1"));
    }
}
