import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import models.character.SimCharacter;
import models.location.Location;
import org.junit.jupiter.api.Test;
import services.NotificationService;

class NotificationServiceTest {

    private SimCharacter createSim() {
        return new SimCharacter("Quinn", 26, "Non-binary", new Location("Home", new ArrayList<>()));
    }

    @Test
    void getReturnsEmptyListWhenNoNotificationsExist() {
        assertTrue(NotificationService.get(createSim()).isEmpty());
    }

    @Test
    void addKeepsOnlyLatestFiveNotifications() {
        SimCharacter sim = createSim();

        for (int i = 1; i <= 6; i++) {
            NotificationService.add(sim, "Message " + i);
        }

        assertEquals(List.of("Message 2", "Message 3", "Message 4", "Message 5", "Message 6"),
                NotificationService.get(sim));
    }

    @Test
    void tickExpiresNotificationsAfterTenActions() {
        SimCharacter sim = createSim();
        NotificationService.add(sim, "Soon gone");

        for (int i = 0; i < 9; i++) {
            NotificationService.tick(sim);
        }
        assertEquals(List.of("Soon gone"), NotificationService.get(sim));

        NotificationService.tick(sim);
        assertTrue(NotificationService.get(sim).isEmpty());
    }

    @Test
    void achievementsArePrioritizedAheadOfRegularMessages() {
        SimCharacter sim = createSim();
        NotificationService.add(sim, "Normal one");
        NotificationService.add(sim, "Achievement unlocked: First Win");
        NotificationService.add(sim, "Normal two");

        assertEquals(
                List.of("Achievement unlocked: First Win", "Normal one", "Normal two"),
                NotificationService.get(sim));
    }
}
