package frc.shufflewood.tools.field;

import frc.shufflewood.MessengerAccess;
import processing.core.PGraphics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class LocalizationOverlay extends FieldOverlay {
    private static final double ROBOT_WIDTH = 0.635;
    private static final double ROBOT_HEIGHT = 0.635;

    private double x = 0, y = 0;
    private double rotation = 0;

    public LocalizationOverlay(MessengerAccess msg) {
        msg.listen("Location", this::onMessage);
    }

    private void onMessage(String type, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        try {
            x = in.readDouble();
            y = in.readDouble();
            rotation = in.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(PGraphics g, float pixelsPerMeter) {
        g.noFill();
        g.stroke(0, 0, 255);
        g.strokeWeight(3 / pixelsPerMeter);

        g.pushMatrix();
        {
            g.translate((float) x, (float) y);
            g.rotate((float) -rotation);
            g.rect((float) -ROBOT_WIDTH / 2, (float) -ROBOT_HEIGHT / 2, (float) ROBOT_WIDTH, (float) ROBOT_HEIGHT);

            // Arrow
            float w3 = (float) ROBOT_WIDTH / 3;
            float h3 = (float) ROBOT_HEIGHT / 3;
            g.line(0, -w3, 0, h3);
            g.line(-w3, 0, 0, -h3);
            g.line(w3, 0, 0, -h3);
        }
        g.popMatrix();
    }

    @Override
    public String getName() {
        return "Localization";
    }
}
