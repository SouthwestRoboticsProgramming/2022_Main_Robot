package frc.robot.command.auto.zero_ball;

import frc.robot.command.CommandSequence;
import frc.robot.command.auto.FollowPathCommand;
import frc.robot.command.auto.Path;

public final class ZeroBallAuto extends CommandSequence {
    public ZeroBallAuto() {
        Path path = new Path();
        path.addPoint(0, -3);

        append(new FollowPathCommand(path));
    }
}