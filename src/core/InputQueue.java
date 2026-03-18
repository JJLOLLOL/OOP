package core;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Thread-safe single-producer, single-consumer queue for raw player input.
 *
 * <p>
 * {@link InputThread} offers lines from the blocking {@link java.util.Scanner}
 * call, and {@link GameEngine} polls them non-blockingly each loop iteration so
 * the main thread is never stalled waiting for the player to type.
 */
public class InputQueue {

    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    /**
     * Adds a line of input to the queue. Called by {@link InputThread}.
     *
     * @param input the raw input line
     */
    public void offer(String input) {
        queue.offer(input);
    }

    /**
     * Retrieves and removes the next input line, or returns {@code null}
     * immediately if the queue is empty. Never blocks.
     *
     * @return the next input line, or {@code null}
     */
    public String poll() {
        return queue.poll();
    }
}
