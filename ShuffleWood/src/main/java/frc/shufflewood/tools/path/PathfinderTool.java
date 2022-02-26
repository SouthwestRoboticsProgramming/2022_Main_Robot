package frc.shufflewood.tools.path;

import frc.shufflewood.App;
import frc.shufflewood.MessengerAccess;
import frc.shufflewood.gui.GuiContext;
import frc.shufflewood.gui.Vec2;
import frc.shufflewood.tools.Tool;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PathfinderTool implements Tool {
    private final App app;
    private final MessengerAccess msg;
    private PGraphics g;
    private int lastW = -1, lastH = -1;

    private final double fieldW = 15.8496;
    private final double fieldH = 7.9248;

    private final float pixelsPerMeter = 40;

    private static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private interface Obstacle {
        void draw(PGraphics g);
    }

    private class Circle implements Obstacle {
        double x, y, radius;

        public Circle(double x, double y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public void draw(PGraphics g) {
            g.stroke(255, 128, 0);
            g.strokeWeight(3 / pixelsPerMeter);
            g.noFill();

            g.ellipseMode(PConstants.CENTER);
            g.ellipse((float) x, (float) y, (float) radius * 2, (float) radius * 2);
        }
    }

    private class Rectangle implements Obstacle {
        double x, y, width, height, rotation;

        public Rectangle(double x, double y, double width, double height, double rotation) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
        }

        @Override
        public void draw(PGraphics g) {
            g.stroke(255, 128, 0);
            g.strokeWeight(3/pixelsPerMeter);
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

    private Set<Obstacle> obstacles;
    private List<Point> path;

    public PathfinderTool(App app) {
        this.app = app;
        msg = app.getMessenger();

        msg.listen("Pathfinder:Path", this::messageCallback);
        msg.listen("Pathfinder:SceneData", this::messageCallback);

        obstacles = new HashSet<>();
        path = new ArrayList<>();
    }

    private void messageCallback(String type, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

        try {
            switch (type) {
                case "Pathfinder:Path": {
                    int count = in.readInt();

                    path.clear();
                    for (int i = 0; i < count; i++) {
                        double x = in.readDouble();
                        double y = in.readDouble();

                        path.add(new Point(x, y));
                    }

                    System.out.println(" i got a path");

                    break;
                }
                case "Pathfinder:SceneData": {
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

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawGraphics() {
        g.background(0, 0, 0);
        g.translate(g.width / 2f, g.height / 2f);
        g.scale(pixelsPerMeter);

        msg.sendMessage("Pathfinder:DumpScene", new byte[0]);
        for (Obstacle o : obstacles) {
            o.draw(g);
        }

        g.strokeWeight(3 / pixelsPerMeter);
        g.stroke(0, 255, 255);
        g.beginShape(PConstants.LINE_STRIP);
        for (Point p : path) {
            g.vertex((float) p.x, (float) p.y);
        }
        g.endShape();
    }

    @Override
    public void draw(GuiContext gui) {
        gui.begin("Pathfinder");

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

        gui.end();
    }
}
