package frc.robot.subsystems.climber;

import frc.robot.Robot;
import frc.robot.Scheduler;
import frc.robot.command.climb.ClimbReset;
import frc.robot.control.Input;
import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Climber extends Subsystem {
    public TelescopingArms telescoping;
    public SwingingArms swinging;

    public Climber() {
        telescoping = new TelescopingArms();
        swinging = new SwingingArms();
    }

    @Override
    public void teleopInit() {
        Scheduler.get().scheduleCommand(new ClimbReset());
    }

    @Override
    public void teleopPeriodic() {
        Input input = Robot.INSTANCE.input;

        if (input.getClimberManualControl()) {
            telescoping.manualMove(input.getClimberTele());
        } else {
            telescoping.extendToDistance(Utils.map(input.getClimberTele(), -1, 1, 0, 1));
        }

        swinging.swingToAngle(Utils.map(input.getClimberSwing(), -1, 1, 45, 135));
    
        if (input.getClimberReset()) {
            Scheduler.get().scheduleCommand(new ClimbReset());
        }
    }

    public void zeroAll() {
        telescoping.zero();
        swinging.zero();
    }
}
