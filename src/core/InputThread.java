package core;

import java.util.Scanner;

public class InputThread implements Runnable {

    private final Scanner scanner;
    private volatile boolean running = true;

    public InputThread(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine(); // only THIS thread blocks
            InputQueue.offer(line);
        }
    }

    public void stop() {
        running = false;
    }
}
