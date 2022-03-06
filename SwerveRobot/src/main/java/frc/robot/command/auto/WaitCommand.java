package frc.robot.command.auto;

import frc.robot.command.TimedCommand;

public class WaitCommand extends TimedCommand {
    public WaitCommand(int time) {
        super(time);
    }

    @Override
    protected void runTimed() {}
}
