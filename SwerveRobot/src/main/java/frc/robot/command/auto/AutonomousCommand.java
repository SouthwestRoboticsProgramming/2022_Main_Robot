package frc.robot.command.auto;

import frc.robot.Robot;
import frc.robot.command.CommandSequence;

public final class AutonomousCommand extends CommandSequence {
    public AutonomousCommand() {
        Path path = new Path();
        path.addPoint(0, -0.4);

        // Override control to use distance 3 (auto)
        append(() -> {
            Robot.INSTANCE.shooter.manualDistance = 3;
            return true;
        });
        append(new WaitCommand(75));

        // Shoot
        append(Robot.INSTANCE.shooter.makeShootCommand());
        append(new WaitCommand(50));

        // Move
        append(new FollowPathCommand(path));

        // Return distance control to the driver
        append(() -> {
            Robot.INSTANCE.shooter.manualDistance = -1;
            return true;
        });
    }

    @Override
    public void end() {
        super.end();
        Robot.INSTANCE.shooter.manualDistance = -1;
    }
}
