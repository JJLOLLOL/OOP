package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class InputThreadTest {

    @Test
    void runTransfersAllScannerLinesIntoQueue() {
        InputQueue queue = new InputQueue();
        Scanner scanner = new Scanner(new ByteArrayInputStream("one\ntwo\n".getBytes(StandardCharsets.UTF_8)));
        InputThread inputThread = new InputThread(scanner, queue);

        inputThread.run();

        assertEquals("one", queue.poll());
        assertEquals("two", queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void stopPreventsRunLoopFromConsumingInput() {
        InputQueue queue = new InputQueue();
        Scanner scanner = new Scanner(new ByteArrayInputStream("one\n".getBytes(StandardCharsets.UTF_8)));
        InputThread inputThread = new InputThread(scanner, queue);

        inputThread.stop();
        inputThread.run();

        assertNull(queue.poll());
    }
}
