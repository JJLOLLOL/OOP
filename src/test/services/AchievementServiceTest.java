package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import models.career.CareerList;
import models.skill.SkillType;
import types.AchievementList;
import ui.UITestSupport;

class AchievementServiceTest {

    @Test
    void unlockHasAndGetUnlockedAchievementsTrackState() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        assertTrue(service.unlockAchievement(fixture.player, AchievementList.FIRST_JOB));
        assertTrue(service.hasAchievement(fixture.player, AchievementList.FIRST_JOB));
        assertEquals(Set.of(AchievementList.FIRST_JOB), service.getUnlockedAchievements(fixture.player));
        assertThrows(UnsupportedOperationException.class,
                () -> service.getUnlockedAchievements(fixture.player).add(AchievementList.EVIL));
    }

    @Test
    void evaluateFirstTimeSkillAchievementUnlocksOnlyOncePerSkill() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        assertEquals(List.of(AchievementList.FIRST_PROGRAMMING),
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

        List<AchievementList> career = service.evaluateCareerAchievements(fixture.player);
        List<AchievementList> work = service.evaluateWorkAchievements(fixture.player);

        assertTrue(career.contains(AchievementList.FIRST_JOB));
        assertTrue(career.contains(AchievementList.TECH_TRAILBLAZER));
        assertTrue(career.contains(AchievementList.FIRST_PROMOTION));
        assertTrue(career.contains(AchievementList.SENIOR_STAFF));
        assertTrue(career.contains(AchievementList.CORPORATE_EXECUTIVE));
        assertTrue(work.contains(AchievementList.FIRST_PROGRAMMING));
        assertTrue(work.contains(AchievementList.FIRST_LOGIC));
    }

    @Test
    void evaluateSocialAchievementsUnlocksFriendlyAndEvilWhenAllStatusesMatch() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        AchievementService service = new AchievementService();

        fixture.player.initializeRelationshipWith(fixture.roommate);
        fixture.player.initializeRelationshipWith(fixture.npc);
        fixture.player.changeRelationshipWith(fixture.roommate, 50);
        fixture.player.changeRelationshipWith(fixture.npc, 50);

        List<AchievementList> friendly = service.evaluateSocialAchievements(
                fixture.player,
                List.of(fixture.player, fixture.roommate, fixture.npc),
                fixture.state.getRelationshipService());

        fixture.player.changeRelationshipWith(fixture.roommate, -150);
        fixture.player.changeRelationshipWith(fixture.npc, -150);

        List<AchievementList> evil = service.evaluateSocialAchievements(
                fixture.player,
                List.of(fixture.player, fixture.roommate, fixture.npc),
                fixture.state.getRelationshipService());

        assertEquals(List.of(AchievementList.FRIENDLY), friendly);
        assertEquals(List.of(AchievementList.EVIL), evil);
    }
}
