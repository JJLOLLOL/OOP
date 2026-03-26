package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import models.character.stats.CharacterStats;
import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class CharacterStatsTest {

    @Test
    void constructorCreatesDefaultNeedsAndSkills() {
        CharacterStats stats = new CharacterStats();

        assertEquals(5, stats.getNeedViews().size());
        assertEquals(SkillType.values().length, stats.getSkillViews().size());
        assertEquals(80.0, stats.getNeed(NeedType.HUNGER).getValue());
        assertEquals(1, stats.getSkillLevel(SkillType.LOGIC));
    }

    @Test
    void getNeedAndGetSkillRejectNull() {
        CharacterStats stats = new CharacterStats();

        assertThrows(IllegalArgumentException.class, () -> stats.getNeed(null));
        assertThrows(IllegalArgumentException.class, () -> stats.getSkill(null));
        assertThrows(IllegalArgumentException.class, () -> stats.adjustNeedRaw(null, 5.0));
        assertThrows(IllegalArgumentException.class, () -> stats.adjustSkillXpRaw(null, 5.0));
    }

    @Test
    void rawAdjustersUpdateStoredState() {
        CharacterStats stats = new CharacterStats();

        stats.adjustNeedRaw(NeedType.ENERGY, -20.0);
        int levelsGained = stats.adjustSkillXpRaw(SkillType.PROGRAMMING, 100.0);

        assertEquals(60.0, stats.getNeed(NeedType.ENERGY).getValue());
        assertEquals(1, levelsGained);
        assertEquals(2, stats.getSkillLevel(SkillType.PROGRAMMING));
        assertEquals(0.0, stats.getSkillXp(SkillType.PROGRAMMING));
    }

    @Test
    void viewCollectionsAreUnmodifiable() {
        CharacterStats stats = new CharacterStats();

        assertThrows(UnsupportedOperationException.class, () -> stats.getNeedViews().clear());
        assertThrows(UnsupportedOperationException.class, () -> stats.getSkillViews().clear());
        assertTrue(stats.getNeedViews().contains(stats.getNeed(NeedType.FUN)));
    }
}