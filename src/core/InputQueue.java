package core;

import java.util.concurrent.LinkedBlockingQueue;

public class InputQueue {

    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void offer(String input) {
        queue.offer(input);
    }

    /**
     * Non-blocking — returns null immediately if nothing is queued
     */
    public String poll() {
        return queue.poll();
    }
}
