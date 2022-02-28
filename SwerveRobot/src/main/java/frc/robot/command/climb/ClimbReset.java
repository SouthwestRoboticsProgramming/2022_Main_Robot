package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.TimedCommand;

public class ClimbReset extends TimedCommand {
    public ClimbReset() {
        super(100);
    }

    @Override
    public void runTimed() {
        Robot.INSTANCE.climber.swinging.manualMove(-0.2);
        Robot.INSTANCE.climber.swinging.setResetting(true);
        System.out.println("RESETTING");
    }

    @Override
    public void end() {
        Robot.INSTANCE.climber.swinging.setResetting(false);
        Robot.INSTANCE.climber.swinging.manualMove(0);
        Robot.INSTANCE.climber.zeroAll();
    }
}
