package frc.robot.subsystems.climber;

import frc.robot.control.Input;
import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Climber extends Subsystem {
    private final Input input;

    public TelescopingArms telescoping;
    public SwingingArms swinging;

    public Climber(Input input) {
        this.input = input;

        telescoping = new TelescopingArms();
        swinging = new SwingingArms();
    }

    @Override
    public void teleopPeriodic() {
        if (input.getClimberManualControl()) {
            telescoping.manualMove(input.getClimberTele());
        } else {
            telescoping.extendToDistance(Utils.map(input.getClimberTele(), -1, 1, 0, 1));
        }

        swinging.swingToAngle(Utils.map(input.getClimberSwing(), -1, 1, 45, 135));
    }
}
