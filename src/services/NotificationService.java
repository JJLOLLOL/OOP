package services;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import models.SimCharacter;

/**
 * Manages notifications for all {@link SimCharacter} instances.
 *
 * <p>
 * Notifications expire after {@value #LIFETIME_TICKS} player actions. Capped at
 * 5 live entries per sim; oldest is dropped when exceeded.
 */
public class NotificationService {

    private static final int LIFETIME_TICKS = 10;
    private static final int MAX_NOTIFICATIONS = 5;

    // Per-sim storage — WeakHashMap so sims can be GC'd freely
    private static final Map<SimCharacter, List<Map.Entry<String, Long>>> store
            = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<SimCharacter, Long> ticks
            = Collections.synchronizedMap(new WeakHashMap<>());

    private NotificationService() {
    }

    /**
     * Adds a notification message for the given sim.
     */
    public static void add(SimCharacter sim, String message) {
        List<Map.Entry<String, Long>> list = store.computeIfAbsent(sim, k -> new ArrayList<>());
        long tick = ticks.getOrDefault(sim, 0L);
        list.add(new AbstractMap.SimpleEntry<>(message, tick));
        if (list.size() > MAX_NOTIFICATIONS) {
            list.remove(0);
        }
    }

    /**
     * Called once per player action. Increments the tick counter and removes
     * expired notifications for the given sim.
     */
    public static void tick(SimCharacter sim) {
        long next = ticks.getOrDefault(sim, 0L) + 1;
        ticks.put(sim, next);
        List<Map.Entry<String, Long>> list = store.get(sim);
        if (list == null) {
            return;
        }
        Iterator<Map.Entry<String, Long>> it = list.iterator();
        while (it.hasNext()) {
            if (next - it.next().getValue() >= LIFETIME_TICKS) {
                it.remove();
            }
        }
    }

    /**
     * Returns the live notification messages for the given sim.
     */
    public static List<String> get(SimCharacter sim) {
        List<Map.Entry<String, Long>> list = store.get(sim);
        if (list == null) {
            return List.of();
        }
        List<String> priority = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        for (Map.Entry<String, Long> e : list) {
            String message = e.getKey();
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
