package frc.robot.subsystems.climber;

import frc.robot.control.Input;
import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

import static frc.robot.constants.ClimberConstants.*;

public class Climber extends Subsystem {
    private final Input input;

    public NewTelescopingArm teleLeft, teleRight;
    public NewSwingingArm swingLeft, swingRight;

    public Climber(Input input) {
        this.input = input;

        // telescoping = new NewTelescopingArm();
        // swinging = new NewSwingingArm();
        teleLeft = new NewTelescopingArm(CLIMBER_LEFT_TELE_MOTOR_ONE_ID, CLIMBER_LEFT_TELE_MOTOR_TWO_ID, true);
        teleRight = new NewTelescopingArm(CLIMBER_RIGHT_TELE_MOTOR_ONE_ID, CLIMBER_RIGHT_TELE_MOTOR_TWO_ID, false);
        swingLeft = new NewSwingingArm(CLIMBER_LEFT_SWING_MOTOR_ID);
        swingRight = new NewSwingingArm(CLIMBER_RIGHT_SWING_MOTOR_ID);
    }

    @Override
    public void teleopPeriodic() {
        // if (input.testButton2()) {
        //     telescoping.manualMove(input.getClimbTele());
        // } else {
        //     telescoping.extendToDistance(Utils.map(input.getClimbTele(), -1, 1, 0, 1));
        // }

        // swinging.swingToAngle(Utils.map(input.testSwingingArm(), -1, 1, 45, 135));
    }
}
