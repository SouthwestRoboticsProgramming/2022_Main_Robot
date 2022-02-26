package frc.shufflewood.tools.field;

import frc.shufflewood.App;
import frc.shufflewood.MessengerAccess;
import frc.shufflewood.gui.GuiContext;
import frc.shufflewood.gui.Vec2;
import frc.shufflewood.tools.Tool;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

public class FieldDataViewTool implements Tool {
    public static final float FIELD_WIDTH = 15.8496f;
    public static final float FIELD_HEIGHT = 7.9248f;
    private static final float VIEW_PADDING_PX = 50;

    private final App app;
    private final List<FieldOverlay> overlays;
    private PGraphics g;
    private int lastW = -1, lastH = -1;

    public FieldDataViewTool(App app) {
        this.app = app;
        overlays = new ArrayList<>();

        MessengerAccess msg = app.getMessenger();
        overlays.add(new ObstacleOverlay(msg));
        overlays.add(new PathOverlay(msg));
    }

    private void drawGraphics() {
        float scaleX = (g.width - 2 * VIEW_PADDING_PX) / FIELD_WIDTH;
        float scaleY = (g.height - 2 * VIEW_PADDING_PX) / FIELD_HEIGHT;

        float pixelsPerMeter = Math.min(scaleX, scaleY);

        g.background(0, 0, 0);
        g.translate(g.width / 2f, g.height / 2f);
        g.scale(pixelsPerMeter);
        g.ellipseMode(PConstants.CENTER);

        for (FieldOverlay overlay : overlays) {
            if (overlay.enabled[0]) {
                overlay.draw(g, pixelsPerMeter);
            }
        }
    }

    @Override
    public void draw(GuiContext gui) {
        gui.begin("Field Data");
        if (gui.isWindowFirstAppearing()) {
            gui.setWindowSize(900, 600);
            gui.setWindowCenterPos(app.width / 2f, app.height / 2f);
        }

        gui.beginTable(true, 5, 1);

        Vec2 size = gui.getAvailableContentSize();
        int width = (int) size.x;
        int height = (int) size.y;
        if (width > 0 && height > 0) {
            if (width != lastW || height != lastH) {
                g = app.createGraphics(width, height, PConstants.P2D);
                System.out.println("Resize");
                lastW = width;
                lastH = height;
            }

            g.beginDraw();
            drawGraphics();
            g.endDraw();

            gui.image(g);
        }

        gui.tableNextColumn();

        gui.text("Overlays");
        gui.separator();
        for (FieldOverlay overlay : overlays) {
            gui.checkbox(overlay.enabled);
            gui.sameLine();
            gui.text(overlay.getName());
        }

        gui.endTable();

        gui.end();
    }
}
