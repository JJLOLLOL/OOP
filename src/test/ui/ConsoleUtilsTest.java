package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ConsoleUtilsTest {

    @Test
    void stripAnsiVisibleLengthAndMaxVisibleIgnoreColourCodes() {
        String coloured = ConsoleUtils.BRIGHT_GREEN + "Hello" + ConsoleUtils.RESET;

        assertEquals("", ConsoleUtils.stripAnsi(null));
        assertEquals("Hello", ConsoleUtils.stripAnsi(coloured));
        assertEquals(5, ConsoleUtils.visibleLength(coloured));
        assertEquals(6, ConsoleUtils.maxVisible(List.of(coloured, "longer")));
    }

    @Test
    void padAndCenterHelpersAlignAndTruncateText() {
        String coloured = ConsoleUtils.BRIGHT_CYAN + "Hi" + ConsoleUtils.RESET;

        assertEquals(coloured + "  ", ConsoleUtils.padColoured(coloured, 4));
        assertEquals("Long", ConsoleUtils.padColoured(ConsoleUtils.BRIGHT_RED + "Longer" + ConsoleUtils.RESET, 4));
        assertEquals("Hi  ", ConsoleUtils.pad("Hi", 4));
        assertEquals("Long", ConsoleUtils.pad("Longer", 4));
        assertEquals("  Hi  ", ConsoleUtils.center("Hi", 6));
        assertEquals(" " + coloured + " ", ConsoleUtils.centerColoured(coloured, 4));
    }

    @Test
    void wordWrapFormatHoursAndSegReturnExpectedStrings() {
        assertEquals(List.of("alpha beta", "gamma"), ConsoleUtils.wordWrap("alpha beta gamma", 10));
        assertEquals("30min", ConsoleUtils.formatHours(0.5));
        assertEquals("1h 30min", ConsoleUtils.formatHours(1.5));
        assertEquals("2h", ConsoleUtils.formatHours(2.0));
        assertEquals("───", ConsoleUtils.seg(3));
    }

    @Test
    void barAndClearScreenProduceExpectedOutput() throws Exception {
        String renderedBar = ConsoleUtils.bar("Fun", 5, 50, 100, ConsoleUtils.BRIGHT_GREEN, "50%");

        assertTrue(ConsoleUtils.stripAnsi(renderedBar).contains("Fun"));
        assertTrue(ConsoleUtils.stripAnsi(renderedBar).contains("[#####-----]"));
        assertTrue(ConsoleUtils.stripAnsi(renderedBar).contains("50%"));

        assertEquals("\033[H\033[2J", UITestSupport.captureOutput(ConsoleUtils::clearScreen));
    }
}
