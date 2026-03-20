package models.DoneTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import models.Location;
import models.SimCharacter;
import org.junit.Test;

import Types.AchievementList;
import Types.CareerList;
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

        List<AchievementList> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.isEmpty());
        assertFalse(sim.hasAchievement(AchievementList.FIRST_JOB));
    }

    @Test
    public void testFirstJobAndTechCareerAchievementUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.SOFTWARE_DEVELOPER);

        List<AchievementList> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.contains(AchievementList.FIRST_JOB));
        assertTrue(unlocked.contains(AchievementList.TECH_TRAILBLAZER));
    }

    @Test
    public void testFirstJobAndDoctorCareerAchievementUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.DOCTOR);

        List<AchievementList> unlocked = achievementService.evaluateCareerAchievements(sim);

        assertTrue(unlocked.contains(AchievementList.FIRST_JOB));
        assertTrue(unlocked.contains(AchievementList.HEALING_HANDS));
    }

    @Test
    public void testFirstJobUnlocksOnlyOnce() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.TEACHER);

        List<AchievementList> firstUnlock = achievementService.evaluateCareerAchievements(sim);
        List<AchievementList> secondUnlock = achievementService.evaluateCareerAchievements(sim);

        assertTrue(firstUnlock.contains(AchievementList.FIRST_JOB));
        assertFalse(secondUnlock.contains(AchievementList.FIRST_JOB));
    }

    @Test
    public void testPromotionMilestoneAchievementsUnlock() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.ENGINEER);

        achievementService.evaluateCareerAchievements(sim);

        sim.updateCareer(100000.0);
        List<AchievementList> rank2Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank2Unlock.contains(AchievementList.FIRST_PROMOTION));

        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        List<AchievementList> rank4Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank4Unlock.contains(AchievementList.SENIOR_STAFF));

        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        sim.updateCareer(100000.0);
        List<AchievementList> rank7Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank7Unlock.contains(AchievementList.CORPORATE_EXECUTIVE));
    }
}
