package core;

import java.util.Scanner;

/**
 * Background thread that reads lines from {@link System#in} and forwards them
 * to an {@link InputQueue}.
 *
 * <p>
 * {@link Scanner#nextLine()} is a blocking call. Running it here keeps the
 * block off the main game-loop thread, so the clock and need decay continue
 * advancing even while the player is mid-input.
 *
 * <p>
 * The thread is started as a daemon thread by {@link GameEngine} so the JVM can
 * exit cleanly without needing to interrupt it explicitly.
 */
public class InputThread implements Runnable {

    private final Scanner scanner;
    private final InputQueue inputQueue;
    private volatile boolean running = true;

    /**
     * Creates a background reader for terminal input.
     *
     * @param scanner the scanner wrapping {@code System.in}
     * @param inputQueue the queue to forward input lines into
     */
    public InputThread(Scanner scanner, InputQueue inputQueue) {
        this.scanner = scanner;
        this.inputQueue = inputQueue;
    }

    @Override
    /**
     * Continuously reads terminal lines and forwards them into the shared
     * input queue.
     */
    public void run() {
        while (running && scanner.hasNextLine()) {
            inputQueue.offer(scanner.nextLine());
        }
    }

    /**
     * Signals the thread to stop accepting new input on its next loop
     * iteration.
     */
    public void stop() {
        running = false;
    }
}
