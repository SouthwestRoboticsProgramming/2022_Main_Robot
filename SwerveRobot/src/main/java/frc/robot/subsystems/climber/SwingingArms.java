package frc.robot.subsystems.climber;

import static frc.robot.constants.ClimberConstants.*;

public class SwingingArms {
    public NewSwingingArm left;
    public NewSwingingArm right;

    public SwingingArms() {
        left = new NewSwingingArm(CLIMBER_LEFT_SWING_MOTOR_ID);
        right = new NewSwingingArm(CLIMBER_RIGHT_SWING_MOTOR_ID);
    }

    public void swingToAngle(double degrees, boolean loaded) {
        left.swingToAngle(degrees, loaded);
        right.swingToAngle(degrees, loaded);
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

    public void zero() {
        left.zero();
        right.zero();
    }

    public void setResetting(boolean resetting) {
        left.setResetting(resetting);
    }
}
