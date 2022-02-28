package frc.robot.command.auto;

import frc.robot.command.CommandSequence;

public final class AutonomousCommand extends CommandSequence {
    public AutonomousCommand() {
        Path path = new Path();
        path.addPoint(0, 5);

        append(new FollowPathCommand(path));
    }
}
