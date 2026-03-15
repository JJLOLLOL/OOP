package core;

import java.util.concurrent.LinkedBlockingQueue;

public class InputQueue {

    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static void offer(String input) {
        queue.offer(input);
    }

    /**
     * Non-blocking — returns null immediately if nothing is queued
     */
    public static String poll() {
        return queue.poll();
    }
}
