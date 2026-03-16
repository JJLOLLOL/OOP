package core;

import java.util.Scanner;

public class InputThread implements Runnable {

    private final Scanner scanner;
    private final InputQueue inputQueue;
    private volatile boolean running = true;

    public InputThread(Scanner scanner, InputQueue inputQueue) {
        this.scanner = scanner;
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine(); // only THIS thread blocks
            inputQueue.offer(line);
        }
    }

    public void stop() {
        running = false;
    }
}
