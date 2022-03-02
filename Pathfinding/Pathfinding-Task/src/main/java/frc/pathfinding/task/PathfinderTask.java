package frc.pathfinding.task;

import frc.messenger.client.MessengerClient;
import frc.pathfinding.lib.Cell;
import frc.pathfinding.lib.PathOptimizer;
import frc.pathfinding.lib.Pathfinder;
import frc.pathfinding.lib.collision.CircleCollider;
import frc.pathfinding.lib.collision.Collider;
import frc.pathfinding.lib.collision.CollisionGrid;
import frc.pathfinding.lib.collision.RectangleCollider;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public final class PathfinderTask {
    private static final String CONFIG_FILE = "config.properties";
    private static final String IN_SET_POSITION = "Pathfinder:SetPosition";
    private static final String IN_SET_TARGET = "Pathfinder:SetTarget";
    private static final String IN_DUMP_SCENE = "Pathfinder:DumpScene";
    private static final String OUT_PATH = "Pathfinder:Path";
    private static final String OUT_SCENE_DATA = "Pathfinder:SceneData";

    private static final int CELLS_X = 54 * 12 / 6;
    private static final int CELLS_Y = 27 * 12 / 6;

    private static final float CELLS_PER_METER = 3.281f * 2;
    private static final float METERS_PER_CELL = 1 / CELLS_PER_METER;

    private void run() {
        Properties config = new Properties();
        try {
            config.load(new FileReader(CONFIG_FILE));
        } catch (IOException e) {
            System.err.println("Failed to load configuration file, using default config");
            config.setProperty("host", "localhost");
            config.setProperty("port", "5805");
            config.setProperty("name", "Pathfinder");
            config.setProperty("agentRadius", "5.89");
            config.setProperty("collisionPadding", "1");
            try {
                config.store(new FileWriter(CONFIG_FILE), "Configuration for Pathfinder");
            } catch (IOException e2) {
                System.err.println("Failed to save default config file:");
                e2.printStackTrace();
            }
        }

        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        String name = config.getProperty("name");
        float agentRadius = Float.parseFloat(config.getProperty("agentRadius"));
        float collisionPadding = Float.parseFloat(config.getProperty("collisionPadding"));

        System.out.println("Connecting to Messenger server at " + host + ":" + port + " as " + name);
        MessengerClient msg = new MessengerClient(host, port, name);
        System.out.println("Connected");

        Collider robotCollider = new CircleCollider(0, 0, agentRadius + collisionPadding);

        CollisionGrid grid = new CollisionGrid(CELLS_X, CELLS_Y, robotCollider);
        grid.loadFromFile("scene.txt");
        Pathfinder pathfinder = new Pathfinder(grid);
        PathOptimizer optimizer = new PathOptimizer(grid);
        // Default start and goal so it doesn't crash
        pathfinder.setStartCell(new Cell(28, 17));
        pathfinder.setGoalCell(new Cell(77, 33));

        msg.listen(IN_SET_TARGET);
        msg.listen(IN_SET_POSITION);
        msg.listen(IN_DUMP_SCENE);
        msg.setCallback((type, data) -> {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            try {
                switch (type) {
                    case IN_SET_TARGET: {
                        double x = in.readDouble();
                        double y = in.readDouble();

                        int cx = (int) (x / METERS_PER_CELL + CELLS_X / 2f);
                        int cy = (int) (y / METERS_PER_CELL + CELLS_Y / 2f);

                        pathfinder.setGoalCell(new Cell(cx, cy));
                        System.out.println("Now targeting cell " + x + ", " + y);
                    }
                    case IN_SET_POSITION: {
                        double x = in.readDouble();
                        double y = in.readDouble();

                        int cx = (int) (x / METERS_PER_CELL + CELLS_X / 2f);
                        int cy = (int) (y / METERS_PER_CELL + CELLS_Y / 2f);

                        pathfinder.setStartCell(new Cell(cx, cy));
                    }
                    case IN_DUMP_SCENE: {
                        final int TYPE_CODE_NONE = -1;
                        final int TYPE_CODE_CIRCLE = 0;
                        final int TYPE_CODE_RECTANGLE = 1;

                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                        DataOutputStream d = new DataOutputStream(b);
                        try {
                            Set<Collider> colliders = grid.getObstacles();
                            d.writeInt(colliders.size());
                            for (Collider c : colliders) {
                                if (c instanceof CircleCollider) {
                                    d.writeInt(TYPE_CODE_CIRCLE);

                                    CircleCollider circle = (CircleCollider) c;
                                    d.writeDouble((circle.getX() - CELLS_X / 2f) / CELLS_PER_METER);
                                    d.writeDouble((circle.getY() - CELLS_Y / 2f) / CELLS_PER_METER);
                                    d.writeDouble(circle.getRadius() / CELLS_PER_METER);
                                } else if (c instanceof RectangleCollider) {
                                    d.writeInt(TYPE_CODE_RECTANGLE);

                                    RectangleCollider rect = (RectangleCollider) c;
                                    d.writeDouble((rect.getX() - CELLS_X / 2f) / CELLS_PER_METER);
                                    d.writeDouble((rect.getY() - CELLS_Y / 2f) / CELLS_PER_METER);
                                    d.writeDouble(rect.getWidth() / CELLS_PER_METER);
                                    d.writeDouble(rect.getHeight() / CELLS_PER_METER);
                                    d.writeDouble(rect.getRotation());
                                } else {
                                    d.writeInt(TYPE_CODE_NONE);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        msg.sendMessage(OUT_SCENE_DATA, b.toByteArray());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while (true) {
            msg.read();

            // Calculate path
            List<Cell> path = pathfinder.getPath();
            path = optimizer.optimize(path);

            // Send path data
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream d = new DataOutputStream(b);
            try {
                d.writeInt(path.size());
                for (Cell cell : path) {
                    double x = (cell.getX() - CELLS_X / 2.0) / CELLS_PER_METER;
                    double y = (cell.getY() - CELLS_Y / 2.0) / CELLS_PER_METER;

                    d.writeDouble(x);
                    d.writeDouble(y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.sendMessage(OUT_PATH, b.toByteArray());

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    public static void main(final String[] args) {
        new PathfinderTask().run();
    }
}
