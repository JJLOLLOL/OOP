package data.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.need.NeedType;
import models.skill.SkillType;

class FurnitureParserTest {

    @Test
    void parse_shouldBuildFurnitureAliasesAndAttachActions() throws IOException {
        Map<String, Furniture> furnitureMap = new FurnitureParser().parse("test/fixtures/data/furniture-fixture.txt");

        Furniture desk = furnitureMap.get("Test Desk");
        Furniture deskAlias = furnitureMap.get("TestDesk");
        Furniture arcade = furnitureMap.get("ArcadeMachine");

        assertEquals(4, furnitureMap.size());
        assertNotNull(desk);
        assertSame(desk, deskAlias);
        assertNotNull(arcade);

        FurnitureAction codeAction = desk.getAction("Code");
        assertNotNull(codeAction);
        assertEquals(-10.5, codeAction.affectedNeedsByActionMap().get(NeedType.ENERGY), 0.001);
        assertEquals(5.0, codeAction.affectedNeedsByActionMap().get(NeedType.FUN), 0.001);
        assertEquals(12.0, codeAction.affectedSkillsByActionMap().get(SkillType.PROGRAMMING), 0.001);
        assertEquals(4.0, codeAction.affectedSkillsByActionMap().get(SkillType.LOGIC), 0.001);

        assertTrue(arcade.getActionNames().contains("Play"));
    }
}
