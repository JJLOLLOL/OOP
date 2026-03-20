package models.DoneTest;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import Types.AchievementType;
import Types.CareerList;
import Types.InteractionType;
import core.GameState;
import core.GameClock;
import core.PlayController;
import core.WorldRegistry;
import models.Location;
import models.NPCCharacter;
import models.SimCharacter;
import org.junit.Test;
import services.AchievementService;
import services.NotificationService;
import services.RelationshipService;
import services.WorkService;

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
        assertFalse(achievementService.hasAchievement(sim, AchievementType.FIRST_JOB));
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

        sim.getCareer().addProgress(100000.0);
        List<AchievementType> rank2Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank2Unlock.contains(AchievementType.FIRST_PROMOTION));

        sim.getCareer().addProgress(100000.0);
        sim.getCareer().addProgress(100000.0);
        List<AchievementType> rank4Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank4Unlock.contains(AchievementType.SENIOR_STAFF));

        sim.getCareer().addProgress(100000.0);
        sim.getCareer().addProgress(100000.0);
        sim.getCareer().addProgress(100000.0);
        List<AchievementType> rank7Unlock = achievementService.evaluateCareerAchievements(sim);
        assertTrue(rank7Unlock.contains(AchievementType.CORPORATE_EXECUTIVE));
    }

    @Test
    public void testCareerAchievementNotificationsAreAdded() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        sim.joinCareer(CareerList.SOFTWARE_DEVELOPER);

        List<AchievementType> unlocked = achievementService.evaluateCareerAchievements(sim);
        addAchievementNotifications(sim, unlocked);

        List<String> notifications = NotificationService.get(sim);
        assertTrue(notifications.contains("Achievement unlocked: First Job"));
        assertTrue(notifications.contains("Achievement unlocked: Tech Trailblazer"));
    }

    @Test
    public void testAchievementNotificationsAppearBeforeOtherNotifications() {
        SimCharacter sim = createSim();

        NotificationService.add(sim, "Career started: Software Developer. Head to the Office to work!");
        NotificationService.add(sim, "Achievement unlocked: First Job");
        NotificationService.add(sim, "Full shift complete (8 hours). Earned $105.00.");

        List<String> notifications = NotificationService.get(sim);

        assertEquals("Achievement unlocked: First Job", notifications.get(0));
    }

    @Test
    public void testWorkAddsAchievementNotificationsForCareerAndSkills() {
        SimCharacter sim = createSim();
        AchievementService achievementService = new AchievementService();
        GameClock clock = new GameClock();
        sim.joinCareer(CareerList.ENGINEER);
        clock.advanceHours(1.0);

        String result = WorkService.work(sim, clock, achievementService);
        NotificationService.add(sim, result);

        List<String> notifications = NotificationService.get(sim);

        assertTrue(notifications.contains("Achievement unlocked: First Job"));
        assertTrue(notifications.contains("Achievement unlocked: Tech Trailblazer"));
        assertTrue(notifications.contains("Achievement unlocked: First Logic"));
        assertTrue(notifications.contains("Achievement unlocked: First Programming"));
        assertEquals("Achievement unlocked: First Job", notifications.get(0));
    }

    @Test
    public void testNotificationsExpireAfterTenTicks() {
        SimCharacter sim = createSim();

        NotificationService.add(sim, "Achievement unlocked: First Job");
        for (int i = 0; i < 10; i++) {
            NotificationService.tick(sim);
        }

        assertTrue(NotificationService.get(sim).isEmpty());
    }

    @Test
    public void testFriendlyHiddenAchievementUnlocksWhenFriendsWithAllOthers() {
        SimCharacter sim = createSim();
        SimCharacter bob = createSim("Bob");
        SimCharacter clara = createSim("Clara");
        AchievementService achievementService = new AchievementService();
        RelationshipService relationshipService = new RelationshipService();

        makeFriends(relationshipService, sim, bob);
        makeFriends(relationshipService, sim, clara);

        List<AchievementType> unlocked = achievementService.evaluateSocialAchievements(
                sim,
                List.of(sim, bob, clara),
                relationshipService);
        addAchievementNotifications(sim, unlocked);

        assertTrue(unlocked.contains(AchievementType.FRIENDLY));
        assertTrue(achievementService.hasAchievement(sim, AchievementType.FRIENDLY));
        assertTrue(NotificationService.get(sim).contains("Achievement unlocked: Friendly"));
    }

    @Test
    public void testEvilHiddenAchievementUnlocksWhenEnemiesWithAllOthers() {
        SimCharacter sim = createSim();
        SimCharacter bob = createSim("Bob");
        SimCharacter clara = createSim("Clara");
        AchievementService achievementService = new AchievementService();
        RelationshipService relationshipService = new RelationshipService();

        makeEnemies(relationshipService, sim, bob);
        makeEnemies(relationshipService, sim, clara);

        List<AchievementType> unlocked = achievementService.evaluateSocialAchievements(
                sim,
                List.of(sim, bob, clara),
                relationshipService);
        addAchievementNotifications(sim, unlocked);

        assertTrue(unlocked.contains(AchievementType.EVIL));
        assertTrue(achievementService.hasAchievement(sim, AchievementType.EVIL));
        assertTrue(NotificationService.get(sim).contains("Achievement unlocked: Evil"));
    }

    @Test
    public void testFriendlyHiddenAchievementUnlocksThroughPlayControllerFlow() throws Exception {
        resetPlayControllerState();

        Location home = new Location("Home", new ArrayList<>());
        SimCharacter sim = createSim("Alice", home);
        SimCharacter bob = createSim("Bob", home);
        SimCharacter clara = createSim("Clara", home);
        GameState state = createPlayingState(sim, bob, clara);
        WorldRegistry world = new EmptyNpcWorldRegistry();

        for (int i = 0; i < 5; i++) {
            performSocialInteraction("1", "2", state, world);
        }
        for (int i = 0; i < 5; i++) {
            performSocialInteraction("2", "2", state, world);
        }

        assertTrue(state.getAchievementService().hasAchievement(sim, AchievementType.FRIENDLY));
        assertTrue(NotificationService.get(sim).contains("Achievement unlocked: Friendly"));
        assertEquals(PlayController.Step.MAIN, PlayController.getStep());
    }

    @Test
    public void testEvilHiddenAchievementUnlocksThroughPlayControllerFlow() throws Exception {
        resetPlayControllerState();

        Location home = new Location("Home", new ArrayList<>());
        SimCharacter sim = createSim("Alice", home);
        SimCharacter bob = createSim("Bob", home);
        SimCharacter clara = createSim("Clara", home);
        GameState state = createPlayingState(sim, bob, clara);
        WorldRegistry world = new EmptyNpcWorldRegistry();

        for (int i = 0; i < 4; i++) {
            performSocialInteraction("1", "4", state, world);
        }
        for (int i = 0; i < 4; i++) {
            performSocialInteraction("2", "4", state, world);
        }

        assertTrue(state.getAchievementService().hasAchievement(sim, AchievementType.EVIL));
        assertTrue(NotificationService.get(sim).contains("Achievement unlocked: Evil"));
        assertEquals(PlayController.Step.MAIN, PlayController.getStep());
    }

    private void addAchievementNotifications(SimCharacter sim, List<AchievementType> unlockedAchievements) {
        for (AchievementType achievement : unlockedAchievements) {
            NotificationService.add(sim, "Achievement unlocked: " + achievement.getTitle());
        }
    }

    private SimCharacter createSim(String name) {
        Location defaultLocation = new Location("Home", new ArrayList<>());
        return new SimCharacter(name, 20, "Female", defaultLocation);
    }

    private SimCharacter createSim(String name, Location location) {
        return new SimCharacter(name, 20, "Female", location);
    }

    private GameState createPlayingState(SimCharacter activePlayer, SimCharacter... others) {
        GameState state = new GameState();
        state.setPhase(GameState.Phase.PLAYING);
        state.addSim(activePlayer);
        for (SimCharacter other : others) {
            state.addSim(other);
        }
        state.setActivePlayer(activePlayer);
        return state;
    }

    private void performSocialInteraction(
            String characterChoice,
            String interactionChoice,
            GameState state,
            WorldRegistry world) {
        assertTrue(PlayController.handleInput("2", state, world));
        assertTrue(PlayController.handleInput(characterChoice, state, world));
        assertTrue(PlayController.handleInput(interactionChoice, state, world));
    }

    private void resetPlayControllerState() throws Exception {
        setStaticField("step", PlayController.Step.MAIN);
        setStaticField("selectedFurniture", null);
        setStaticField("selectedCharacter", null);
    }

    private void setStaticField(String fieldName, Object value) throws Exception {
        Field field = PlayController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    private void makeFriends(RelationshipService relationshipService, SimCharacter from, SimCharacter to) {
        for (int i = 0; i < 5; i++) {
            relationshipService.interact(from, to, InteractionType.COMPLIMENT);
        }
    }

    private void makeEnemies(RelationshipService relationshipService, SimCharacter from, SimCharacter to) {
        for (int i = 0; i < 4; i++) {
            relationshipService.interact(from, to, InteractionType.INSULT);
        }
    }

    private static class EmptyNpcWorldRegistry extends WorldRegistry {

        @Override
        public List<NPCCharacter> getAllNPCs() {
            return List.of();
        }
    }
}
