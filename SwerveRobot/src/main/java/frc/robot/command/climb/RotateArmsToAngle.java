package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.Command;

public class RotateArmsToAngle implements Command {
    private final double angle;

    public RotateArmsToAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public boolean run() {
        Robot.INSTANCE.climber.swinging.swingToAngle(angle);

        return false;
    }
}
