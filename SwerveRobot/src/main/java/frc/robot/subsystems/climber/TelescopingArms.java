package frc.robot.subsystems.climber;

import static frc.robot.constants.ClimberConstants.*;

public class TelescopingArms {
    public NewTelescopingArm left;
    public NewTelescopingArm right;

    public TelescopingArms() {
        left = new NewTelescopingArm(
            CLIMBER_LEFT_TELE_MOTOR_ONE_ID,
            CLIMBER_LEFT_TELE_MOTOR_TWO_ID, 
            true
        );
        right = new NewTelescopingArm(
            CLIMBER_RIGHT_TELE_MOTOR_ONE_ID,
            CLIMBER_RIGHT_TELE_MOTOR_TWO_ID,
            false
        );
    }

    // Distance 0 to 1
    public void extendToDistance(double distance, boolean loaded) {
        left.extendToDistance(distance, loaded);
        right.extendToDistance(distance, loaded);
    }

    public void manualMove(double amount) {
        left.manualMove(amount);
        right.manualMove(amount);
    }

    public void resetEnc() {
        left.resetEnc();
        right.resetEnc();
    }

    public void stop() {
        left.stop();
        right.stop();
    }
}
