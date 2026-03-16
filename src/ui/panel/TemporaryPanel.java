package ui.panel;

import java.util.ArrayList;
import java.util.List;

public class TemporaryPanel implements Panel {

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();
        lines.add("This is a temporary panel");
        return lines;
    }
}
