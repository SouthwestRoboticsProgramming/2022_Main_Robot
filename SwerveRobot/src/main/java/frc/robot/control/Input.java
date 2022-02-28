package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Input extends Subsystem {
    private final XboxController drive;
    private final XboxController manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER);
        manipulator = new XboxController(11);
    }

    /* Drive */
    public double getDriveX() {
        return mapJoystick(drive.leftStickX.get());
    }

    public double getDriveY() {
        return mapJoystick(drive.leftStickY.get());
    }

    public double getRot() {
        return mapJoystick(drive.rightStickX.get());
    }

    /* Intake */
    public boolean getIntake() {
        return manipulator.leftShoulder.isPressed();
    }

    // Unused
    private boolean intakeLift = true;
    @Deprecated
    public boolean getIntakeLift() {

        /* Get leading edge */
        boolean pressed = manipulator.y.leadingEdge();

        /* If it's pressed, toggle the intake */
        if (pressed) {
            intakeLift = !intakeLift;
        }

        return intakeLift;
    }

    /* Shooter */
    public boolean getShoot() {
        return manipulator.a.leadingEdge();
    }

    // Unused
    @Deprecated
    public boolean getAim() {
        return drive.rightShoulder.isPressed() || manipulator.rightShoulder.isPressed();
    }

    public int getShootDistance() {
        if (manipulator.dpadUp.isPressed()) return 0;
        if (manipulator.dpadDown.isPressed()) return 2;
        return 1;
    }

    /* Climber */
    // TODO: Put the climber stuff here

    /* Tools */
    private double mapJoystick(double amount) {
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }

        return Math.signum(amount) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE, 1, 0, 1);
    }
}
