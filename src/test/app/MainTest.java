package app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void mainRunsThroughCreateSimFlowAndQuits() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            String output = captureOutputWithInput(
                    "1\nAlex\n25\nM\nY\n6\n",
                    () -> Main.main(new String[0]));

            assertTrue(output.contains("CREATE YOUR SIMS"));
            assertTrue(output.contains("Actions"));
            assertTrue(output.contains("Game over. Thanks for playing!"));
        });
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static String captureOutputWithInput(String input, ThrowingRunnable action) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capture);
            action.run();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
