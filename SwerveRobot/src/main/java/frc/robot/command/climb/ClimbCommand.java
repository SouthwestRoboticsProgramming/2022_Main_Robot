package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.CommandSequence;
import frc.robot.util.ShuffleBoard;

public class ClimbCommand extends CommandSequence {
    public ClimbCommand() {
        // Add the climb steps here

        //0 = arms down, ready to lift
        // PAUSE
        //1 = arms extended, ready to clamp onto 2
        //2 = arms retract, ready for rotator
        //3 = rotator arms rotate robot to face 3
        //4 = extend arms to clamp onto 3
        //5 = rotator applies pressure on extender arms
        // PAUSE
        //GoTo step 2

        append(new ExtendArmsToLength(ShuffleBoard.climbTune1TeleHeight.getDouble(0)));
        append(new ExtendArmsToLength(ShuffleBoard.climbTune2TeleHeight.getDouble(0)));
    }

    @Override
    public boolean run() {
        if (Robot.INSTANCE.input.getClimberNextStep()) {
            next();
        }

        return super.run();
    }
}
