package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.CommandSequence;

public class ClimbCommand extends CommandSequence {
    public ClimbCommand() {
        // Add the climb steps here
    }

    @Override
    public boolean run() {
        if (Robot.INSTANCE.input.getClimberNextStep()) {
            next();
        }

        return super.run();
    }
}
