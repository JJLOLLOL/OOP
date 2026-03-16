package models.DoneTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import models.AchievementType;
import models.CareerList;
import models.Location;
import models.SimCharacter;
import org.junit.Test;
import services.AchievementService;

public class AchievementServiceTest {

    private SimCharacter createSim() {
        Location defaultLocation = new Location("Home", new ArrayList<>());
        return new SimCharacter("Alice", 20, "Female", defaultLocation);
    }

    @Test
    public void testCareerAchievementsStayLockedWhenJobless() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();

        List<AchievementType> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.isEmpty());
        assertFalse(sim.hasAchievement(AchievementType.FIRST_JOB));
    }

    @Test
    public void testFirstJobAndTechCareerAchievementUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.SOFTWARE_DEVELOPER);

        List<AchievementType> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.contains(AchievementType.FIRST_JOB));
        assertTrue(unlocked.contains(AchievementType.TECH_TRAILBLAZER));
    }

    @Test
    public void testFirstJobAndDoctorCareerAchievementUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.DOCTOR);

        List<AchievementType> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.contains(AchievementType.FIRST_JOB));
        assertTrue(unlocked.contains(AchievementType.HEALING_HANDS));
    }

    @Test
    public void testFirstJobUnlocksOnlyOnce() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.TEACHER);

        List<AchievementType> firstUnlock = achievementService.evaluateCareerAchievements(sim);
        List<AchievementType> secondUnlock = achievementService.evaluateCareerAchievements(sim);

        assertTrue(firstUnlock.contains(AchievementType.FIRST_JOB));
        assertFalse(secondUnlock.contains(AchievementType.FIRST_JOB));
    }

    @Test
    public void testPromotionMilestoneAchievementsUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.ENGINEER);

        achievementService.evaluateCareerAchievements(sim);

        sim.updateCareer(100000.0);
        List<AchievementType> rank2Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank2Unlock.contains(AchievementType.FIRST_PROMOTION));

        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        List<AchievementType> rank4Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank4Unlock.contains(AchievementType.SENIOR_STAFF));

        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        List<AchievementType> rank7Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank7Unlock.contains(AchievementType.CORPORATE_EXECUTIVE));
    }
}
