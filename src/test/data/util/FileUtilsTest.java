package data.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class FileUtilsTest {

    @Test
    void readFile_shouldReturnFixtureLines_whenFallbackPathExists() throws IOException {
        List<String> lines = FileUtils.readFile("test/fixtures/data/file-utils-fixture.txt");

        assertEquals(List.of("alpha", "beta", "gamma"), lines);
    }

    @Test
    void readFile_shouldThrowIOException_whenResourceDoesNotExist() {
        IOException exception = assertThrows(
                IOException.class,
                () -> FileUtils.readFile("test/fixtures/data/does-not-exist.txt"));

        assertTrue(exception.getMessage().contains("Resource not found"));
    }
}
