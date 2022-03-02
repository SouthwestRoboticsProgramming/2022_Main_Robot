package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.Command;

public class ExtendArmsToLength implements Command {
    private final double targetLength;

    public ExtendArmsToLength(double targetLength) {
        this.targetLength = targetLength;
    }

    @Override
    public boolean run() {
        Robot.INSTANCE.climber.telescoping.extendToDistance(targetLength);

        return false;
    }
}
