package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import models.career.CareerList;
import models.skill.SkillType;
import types.AchievementType;
import ui.UITestSupport;

class AchievementServiceTest {

    @Test
    void unlockHasAndGetUnlockedAchievementsTrackState() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        assertTrue(service.unlockAchievement(fixture.player, AchievementType.FIRST_JOB));
        assertTrue(service.hasAchievement(fixture.player, AchievementType.FIRST_JOB));
        assertEquals(Set.of(AchievementType.FIRST_JOB), service.getUnlockedAchievements(fixture.player));
        assertThrows(UnsupportedOperationException.class,
                () -> service.getUnlockedAchievements(fixture.player).add(AchievementType.EVIL));
    }

    @Test
    void evaluateFirstTimeSkillAchievementUnlocksOnlyOncePerSkill() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        assertEquals(List.of(AchievementType.FIRST_PROGRAMMING),
                service.evaluateFirstTimeSkillAchievement(fixture.player, SkillType.PROGRAMMING));
        assertEquals(List.of(),
                service.evaluateFirstTimeSkillAchievement(fixture.player, SkillType.PROGRAMMING));
    }

    @Test
    void evaluateCareerAndWorkAchievementsUnlockCareerTypeRankAndSkillAwards() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();
        fixture.player.joinCareer(CareerList.SOFTWARE_DEVELOPER);
        for (int i = 0; i < 6; i++) {
            fixture.player.getCareer().addProgress(10_000);
        }

        List<AchievementType> career = service.evaluateCareerAchievements(fixture.player);
        List<AchievementType> work = service.evaluateWorkAchievements(fixture.player);

        assertTrue(career.contains(AchievementType.FIRST_JOB));
        assertTrue(career.contains(AchievementType.TECH_TRAILBLAZER));
        assertTrue(career.contains(AchievementType.FIRST_PROMOTION));
        assertTrue(career.contains(AchievementType.SENIOR_STAFF));
        assertTrue(career.contains(AchievementType.CORPORATE_EXECUTIVE));
        assertTrue(work.contains(AchievementType.FIRST_PROGRAMMING));
        assertTrue(work.contains(AchievementType.FIRST_LOGIC));
    }

    @Test
    void evaluateSocialAchievementsUnlocksFriendlyAndEvilWhenAllStatusesMatch() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        fixture.player.initializeRelationshipWith(fixture.roommate);
        fixture.player.initializeRelationshipWith(fixture.npc);
        fixture.player.changeRelationshipWith(fixture.roommate, 50);
        fixture.player.changeRelationshipWith(fixture.npc, 50);

        List<AchievementType> friendly = service.evaluateSocialAchievements(
                fixture.player,
                List.of(fixture.player, fixture.roommate, fixture.npc),
                fixture.state.getRelationshipService());

        fixture.player.changeRelationshipWith(fixture.roommate, -150);
        fixture.player.changeRelationshipWith(fixture.npc, -150);

        List<AchievementType> evil = service.evaluateSocialAchievements(
                fixture.player,
                List.of(fixture.player, fixture.roommate, fixture.npc),
                fixture.state.getRelationshipService());

        assertEquals(List.of(AchievementType.FRIENDLY), friendly);
        assertEquals(List.of(AchievementType.EVIL), evil);
    }
}
