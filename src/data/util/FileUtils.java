package data.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * Reads a resource file from the classpath into a list of strings.
     * Falls back to reading from the 'src' directory if not found in classpath.
     */
    public static List<String> readFile(String resourcePath) throws IOException {
        List<String> lines = new ArrayList<>();
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                lines.addAll(reader.lines().toList());
            }
            return lines;
        }

        Path fallbackPath = Path.of("src").resolve(resourcePath);
        if (!Files.exists(fallbackPath)) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        return Files.readAllLines(fallbackPath);
    }
}