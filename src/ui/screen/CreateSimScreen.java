package ui.screen;

import ui.layout.FrameType;
import ui.layout.Region;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimActionPanel;
import ui.panel.CreateSimListPanel;

public class CreateSimScreen implements Screen {

    private static final String TITLE = "CREATE SIM";
    private final ScreenLayout layout = new ScreenLayout(FrameType.DOUBLE_HORIZONTAL);
    private final CreateSimListPanel listPanel = new CreateSimListPanel();
    private final CreateSimActionPanel actionPanel = new CreateSimActionPanel();

    public CreateSimScreen() {
        layout.setPanel(Region.LEFT, listPanel);
        layout.setPanel(Region.RIGHT, actionPanel);
        layout.setInputMode(ScreenLayout.InputMode.CREATESIM);
    }

    @Override
    public void render() {
        layout.render(TITLE);
    }

    public ScreenLayout getLayout() {
        return layout;
    }

    public CreateSimListPanel getListPanel() {
        return listPanel;
    }

    public CreateSimActionPanel getActionPanel() {
        return actionPanel;
    }

    @Override
    public void parkCursor() {
        layout.parkCursor();
    }
}
