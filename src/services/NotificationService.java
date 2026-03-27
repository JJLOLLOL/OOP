package services;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import models.character.SimCharacter;

/**
 * Manages timed notifications for each {@link SimCharacter}. Notifications
 * expire after {@value #LIFETIME_TICKS} player actions, capped at
 * {@value #MAX_NOTIFICATIONS} per sim.
 */
public class NotificationService {

    private static final int LIFETIME_TICKS = 10;
    private static final int MAX_NOTIFICATIONS = 5;

    private record Entry(String message, long tick) {

    }

    private static final Map<SimCharacter, Deque<Entry>> store
            = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<SimCharacter, Long> ticks
            = Collections.synchronizedMap(new WeakHashMap<>());

    private NotificationService() {
    }

    /**
     * Adds a notification for the supplied sim, evicting the oldest item when
     * the per-sim cap is exceeded.
     *
     * @param sim the sim receiving the notification
     * @param message the message to store
     */
    public static void add(SimCharacter sim, String message) {
        Deque<Entry> queue = store.computeIfAbsent(sim, k -> new ArrayDeque<>());
        queue.addLast(new Entry(message, ticks.getOrDefault(sim, 0L)));
        if (queue.size() > MAX_NOTIFICATIONS) {
            queue.pollFirst();
        }
    }

    /**
     * Advances the notification lifetime counter for a sim and expires old
     * notifications.
     *
     * @param sim the sim whose notification queue should age
     */
    public static void tick(SimCharacter sim) {
        long next = ticks.getOrDefault(sim, 0L) + 1;
        ticks.put(sim, next);
        Deque<Entry> queue = store.get(sim);
        if (queue != null) {
            queue.removeIf(e -> next - e.tick() >= LIFETIME_TICKS);
        }
    }

    /**
     * Returns the current visible notifications for a sim, keeping achievement
     * messages at the top of the list.
     *
     * @param sim the sim whose notifications should be returned
     * @return ordered notification messages for display
     */
    public static List<String> get(SimCharacter sim) {
        Deque<Entry> queue = store.get(sim);
        if (queue == null) {
            return List.of();
        }
        List<String> priority = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        for (Entry e : queue) {
            String message = e.message();
            if (message != null && message.startsWith("Achievement unlocked:")) {
                priority.add(message);
            } else {
                messages.add(message);
            }
        }
        priority.addAll(messages);
        return priority;
    }
}
