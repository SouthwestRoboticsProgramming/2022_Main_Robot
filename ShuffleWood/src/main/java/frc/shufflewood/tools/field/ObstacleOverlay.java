package frc.shufflewood.tools.field;

import frc.shufflewood.MessengerAccess;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ObstacleOverlay extends FieldOverlay {
    private interface Obstacle {
        void draw(PGraphics g, float pixelsPerMeter);
    }

    private static class Circle implements Obstacle {
        private final double x, y, radius;

        public Circle(double x, double y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public void draw(PGraphics g, float pixelsPerMeter) {
            g.stroke(255, 128, 0);
            g.strokeWeight(3f / pixelsPerMeter);
            g.noFill();

            g.ellipseMode(PConstants.CENTER);
            g.ellipse((float) x, (float) y, (float) radius * 2, (float) radius * 2);
        }
    }

    private static class Rectangle implements Obstacle {
        private final double x, y, width, height, rotation;

        public Rectangle(double x, double y, double width, double height, double rotation) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
        }

        @Override
        public void draw(PGraphics g, float pixelsPerMeter) {
            g.stroke(255, 128, 0);
            g.strokeWeight(3f / pixelsPerMeter);
            g.noFill();

            g.pushMatrix();
            {
                g.translate((float) x, (float) y);
                g.rotate((float) -rotation);
                g.rect((float) -width / 2, (float) -height / 2, (float) width, (float) height);
            }
            g.popMatrix();
        }
    }

    private final MessengerAccess msg;
    private final Set<Obstacle> obstacles;

    public ObstacleOverlay(MessengerAccess msg) {
        this.msg = msg;
        obstacles = new HashSet<>();

        msg.listen("Pathfinder:SceneData", this::onMessage);
    }

    private void onMessage(String type, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        try {
            int count = in.readInt();

            obstacles.clear();
            for (int i = 0; i < count; i++) {
                int typeCode = in.readInt();

                if (typeCode == 0) {
                    double x = in.readDouble();
                    double y = in.readDouble();
                    double radius = in.readDouble();

                    obstacles.add(new Circle(x, y, radius));
                } else if (typeCode == 1) {
                    double x = in.readDouble();
                    double y = in.readDouble();
                    double width = in.readDouble();
                    double height = in.readDouble();
                    double rotation = in.readDouble();

                    obstacles.add(new Rectangle(x, y, width, height, rotation));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(PGraphics g, float pixelsPerMeter) {
        msg.sendMessage("Pathfinder:DumpScene", new byte[0]);
        for (Obstacle o : obstacles) {
            o.draw(g, pixelsPerMeter);
        }
    }

    @Override
    public String getName() {
        return "Obstacles";
    }
}
