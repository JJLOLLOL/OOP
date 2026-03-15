package ui.screen;

import ui.layout.FrameType;
import ui.layout.Region;
import ui.layout.ScreenLayout;
import ui.panel.ActionPanel;
import ui.panel.AttributePanel;
import ui.panel.NPCPanel;
import ui.panel.Panel;
import ui.panel.TemporaryPanel;

public class MainScreen implements Screen {

    private static final String TITLE = "GAME PAGE";
    private final ScreenLayout layout = new ScreenLayout(FrameType.QUAD);
    private final AttributePanel attributePanel = new AttributePanel();
    private final TemporaryPanel panelBL = new TemporaryPanel();
    private final NPCPanel npcPanel = new NPCPanel();
    private final ActionPanel actionPanel = new ActionPanel();

    public MainScreen() {
        layout.setPanel(Region.TOP_LEFT, attributePanel);
        layout.setPanel(Region.BOTTOM_LEFT, panelBL);
        layout.setPanel(Region.TOP_RIGHT, npcPanel);
        layout.setPanel(Region.BOTTOM_RIGHT, new ActionPanel());
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
    public AttributePanel getAttributePanel() {
        return attributePanel;
    }
    public NPCPanel getNpcPanel() {
        return npcPanel;
    }

    public ActionPanel getPanelBR() {
        return actionPanel;
    }

    public void setActionPanel(Panel panel) {
        layout.setPanel(Region.BOTTOM_RIGHT, panel);
    }

    @Override
    public void parkCursor() {
        layout.parkCursor();
    }
}
