package data.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.need.NeedType;

class ParserUtilsTest {

    @Test
    void parseBlocks_shouldCollectOnlyMatchingBlocks() {
        List<String> lines = List.of(
                "# ignored comment",
                "[FURNITURE]",
                "NAME: Desk",
                "PRICE: 100",
                "",
                "[ACTION]",
                "NAME: Code",
                "[FURNITURE]",
                "NAME: Chair");
        List<Map<String, String>> blocks = new ArrayList<>();

        ParserUtils.parseBlocks(lines, "[FURNITURE]", blocks);

        assertEquals(2, blocks.size());
        assertEquals("Desk", blocks.get(0).get("NAME"));
        assertEquals("100", blocks.get(0).get("PRICE"));
        assertEquals("Chair", blocks.get(1).get("NAME"));
    }

    @Test
    void parseBlocks_shouldIgnoreCommentsBlankLinesAndLinesWithoutColon() {
        List<String> lines = List.of(
                "   ",
                "# comment",
                "[FURNITURE]",
                "NAME: Sofa",
                "INVALID LINE",
                "PRICE: 250",
                "[OTHER]",
                "NAME: Ignored");
        List<Map<String, String>> blocks = new ArrayList<>();

        ParserUtils.parseBlocks(lines, "[FURNITURE]", blocks);

        assertEquals(1, blocks.size());
        assertEquals("Sofa", blocks.get(0).get("NAME"));
        assertEquals("250", blocks.get(0).get("PRICE"));
        assertEquals(2, blocks.get(0).size());
    }

    @Test
    void parseBlocks_shouldAddIntermediateEmptyMatchingBlockButNotFinalEmptyBlock() {
        List<String> lines = List.of(
                "[FURNITURE]",
                "[OTHER]",
                "[FURNITURE]");
        List<Map<String, String>> blocks = new ArrayList<>();

        ParserUtils.parseBlocks(lines, "[FURNITURE]", blocks);

        assertEquals(1, blocks.size());
        assertTrue(blocks.get(0).isEmpty());
    }

    @Test
    void parseEffects_shouldParseKnownEnumsAndSkipInvalidOnes() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        Map<NeedType, Double> effects;
        try {
            System.setErr(new PrintStream(errContent));
            effects = ParserUtils.parseEffects(
                    "ENERGY,10.0;INVALID,4.0;HUNGER,-5.0",
                    value -> NeedType.valueOf(value.toUpperCase()));
        } finally {
            System.setErr(originalErr);
        }

        assertEquals(2, effects.size());
        assertEquals(10.0, effects.get(NeedType.ENERGY), 0.001);
        assertEquals(-5.0, effects.get(NeedType.HUNGER), 0.001);
        assertTrue(errContent.toString().contains("Invalid enum constant in data file: INVALID"));
    }

    @Test
    void parseEffects_shouldReturnEmptyMap_whenInputIsBlank() {
        Map<NeedType, Double> effects = ParserUtils.parseEffects(
                "   ",
                value -> NeedType.valueOf(value.toUpperCase()));

        assertTrue(effects.isEmpty());
    }

    @Test
    void parseEffects_shouldReturnEmptyMap_whenInputIsNull() {
        Map<NeedType, Double> effects = ParserUtils.parseEffects(
                null,
                value -> NeedType.valueOf(value.toUpperCase()));

        assertTrue(effects.isEmpty());
    }

    @Test
    void parseEffects_shouldIgnoreMalformedPairs() {
        Map<NeedType, Double> effects = ParserUtils.parseEffects(
                "ENERGY,10;MALFORMED;HUNGER,-2",
                value -> NeedType.valueOf(value.toUpperCase()));

        assertEquals(2, effects.size());
        assertEquals(10.0, effects.get(NeedType.ENERGY), 0.001);
        assertEquals(-2.0, effects.get(NeedType.HUNGER), 0.001);
    }

    @Test
    void parseEffects_shouldSkipInvalidNumberAndPrintWarning() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        Map<NeedType, Double> effects;
        try {
            System.setErr(new PrintStream(errContent));
            effects = ParserUtils.parseEffects(
                    "ENERGY,abc;HUNGER,-2",
                    value -> NeedType.valueOf(value.toUpperCase()));
        } finally {
            System.setErr(originalErr);
        }

        assertEquals(1, effects.size());
        assertEquals(-2.0, effects.get(NeedType.HUNGER), 0.001);
        assertTrue(errContent.toString().contains("Invalid enum constant in data file: ENERGY"));
    }
}