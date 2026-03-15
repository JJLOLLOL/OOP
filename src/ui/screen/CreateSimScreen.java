package ui.screen;

import ui.layout.FrameType;
import ui.layout.Region;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimPanel;

public class CreateSimScreen implements Screen {

    private static final String TITLE = "CREATE SIM";
    private final ScreenLayout layout = new ScreenLayout(FrameType.SINGLE);
    private final CreateSimPanel panel = new CreateSimPanel();

    public CreateSimScreen() {
        layout.setPanel(Region.MAIN, panel);
    }

    @Override
    public void render() {
        layout.render(TITLE);
    }

    public ScreenLayout getLayout() {
        return layout;
    }

    public CreateSimPanel getPanel() {
        return panel;
    }

    @Override
    public void parkCursor() {
        layout.parkCursor();
    }
}
