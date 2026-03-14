package ui.screen;

import ui.layout.FrameType;
import ui.layout.Region;
import ui.layout.ScreenLayout;
import ui.panel.AttributePanel;
import ui.panel.TemporaryPanel;

public class MainScreen implements Screen {

    private static final String TITLE = "GAME PAGE";
    private final ScreenLayout layout = new ScreenLayout(FrameType.QUAD);
    private final AttributePanel panelTL = new AttributePanel();
    private final TemporaryPanel panelBL = new TemporaryPanel();
    private final TemporaryPanel panelTR = new TemporaryPanel();
    private final TemporaryPanel panelBR = new TemporaryPanel();

    public MainScreen() {
        layout.setPanel(Region.TOP_LEFT, panelTL);
        layout.setPanel(Region.BOTTOM_LEFT, panelBL);
        layout.setPanel(Region.TOP_RIGHT, panelTR);
        layout.setPanel(Region.BOTTOM_RIGHT, panelBR);
    }

    @Override
    public void render() {
        layout.render(TITLE);
    }

    public ScreenLayout getLayout() {
        return layout;
    }

    public TemporaryPanel getPanelBL() {
        return panelBL;
    }
    public AttributePanel getPanelTL() {
        return panelTL;
    }
    public TemporaryPanel getPanelTR() {
        return panelTR;
    }
    public TemporaryPanel getPanelBR() {
        return panelBR;
    }
}
