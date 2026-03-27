package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ui.UITestSupport;

class NotificationServiceTest {

    @Test
    void addCapsNotificationsAtFiveAndPrioritisesAchievements() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        NotificationService.add(fixture.player, "one");
        NotificationService.add(fixture.player, "two");
        NotificationService.add(fixture.player, "three");
        NotificationService.add(fixture.player, "Achievement unlocked: First Job");
        NotificationService.add(fixture.player, "four");
        NotificationService.add(fixture.player, "five");

        List<String> notes = NotificationService.get(fixture.player);

        assertEquals(5, notes.size());
        assertEquals("Achievement unlocked: First Job", notes.get(0));
        assertTrue(notes.contains("two"));
        assertTrue(notes.contains("five"));
    }

    @Test
    void tickExpiresOldNotificationsAfterTenTicks() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        NotificationService.add(fixture.player, "temporary");

        for (int i = 0; i < 199; i++) {
            NotificationService.tick(fixture.player);
        }
        assertEquals(List.of("temporary"), NotificationService.get(fixture.player));

        NotificationService.tick(fixture.player);
        assertEquals(List.of(), NotificationService.get(fixture.player));
    }
}
