package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Input extends Subsystem {
    private final XboxController drive;
    private final XboxController manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER);
    }

    /* Drive */
    public double getDriveX() {
        return mapJoystick(drive.leftStickX.get());
    }

    public double getDriveY() {
        return mapJoystick(drive.leftStickY.get());
    }

    public double getRot() {
        return mapJoystick(-drive.rightStickX.get());
    }

    public boolean getSpeedMode() {
        return drive.rightShoulder.isPressed();
    }

    /* Intake */
    public boolean getIntake() {
        return manipulator.leftShoulder.isPressed();
    }

    // Unused
    private boolean intakeEnable = false;
    public boolean getIntakeEnable() {

        /* Get leading edge */
        boolean pressed = manipulator.y.leadingEdge();

        /* If it's pressed, toggle the intake */
        if (pressed) {
            intakeEnable = !intakeEnable;
        }

        return intakeEnable;
    }

    /* Shooter */
    public boolean getShoot() {
        return manipulator.a.leadingEdge();
    }

    public int getShootDistance() {
        if (manipulator.dpadUp.isPressed()) return 0;
        if (manipulator.dpadDown.isPressed()) return 2;
        return 1;
    }

    /* Climber */
    public double getTeleManual() {
        return mapJoystick(manipulator.leftStickY.get());
    }

    public double getSwingManual() {
        return mapJoystick(manipulator.leftStickX.get());
    }

    public boolean getNextStep() {
        return manipulator.x.leadingEdge();
    }

    public boolean getClimberReset() {
        return manipulator.start.leadingEdge();
    }

    public double getNeoTestLeft() {
        return 0.25 * mapJoystick(manipulator.rightStickX.get());
    }

    public double getNeoTestRight() {
        return 0.25 * mapJoystick(manipulator.rightStickX.get());
    }



    // TODO: Remove
    // public boolean getClimberManualControl() {
    //     return manipulator.select.isPressed();
    // }

    // public double getClimberSwing() {
    //     return mapJoystick(manipulator.leftStickX.get());
    // }

    // public boolean getClimberNextStep() {
    //     return manipulator.leftShoulder.isPressed() && manipulator.rightShoulder.isPressed();
    // }

    /* Tools */
    private double mapJoystick(double amount) {
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }

        return Math.signum(amount) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE, 1, 0, 1);
    }

    @Override
    public void robotPeriodic() {
        drive.update();
        manipulator.update();
    }
}
