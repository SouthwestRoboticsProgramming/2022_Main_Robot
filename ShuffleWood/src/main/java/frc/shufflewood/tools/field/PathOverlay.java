package frc.shufflewood.tools.field;

import frc.shufflewood.MessengerAccess;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathOverlay extends FieldOverlay {
    private static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final List<Point> path;

    public PathOverlay(MessengerAccess msg) {
        path = new ArrayList<>();

        msg.listen("Pathfinder:Path", this::onMessage);
    }

    private void onMessage(String type, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        try {
            int count = in.readInt();

            path.clear();
            for (int i = 0; i < count; i++) {
                double x = in.readDouble();
                double y = in.readDouble();

                path.add(new Point(x, y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("pathy");
    }

    @Override
    public void draw(PGraphics g, float pixelsPerMeter) {
        g.strokeWeight(3f / pixelsPerMeter);
        g.stroke(0, 255, 255);
        g.beginShape(PConstants.LINE_STRIP);
        for (Point p : path) {
            g.vertex((float) p.x, (float) p.y);
        }
        g.endShape();
    }

    @Override
    public String getName() {
        return "Path";
    }
}
