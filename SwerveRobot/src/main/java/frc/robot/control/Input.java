package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Robot;
import frc.robot.command.climb.AutoClimb;
import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Input extends Subsystem {
    private Robot robot;
    private final XboxController drive;
    private final XboxController manipulator;

    public Input(Robot robot) {
        this.robot = robot;
        drive = new XboxController(DRIVE_CONTROLLER);
        manipulator = new XboxController(11);
    }

    /* Drive */
    public double getDriveX() {
        double amount = drive.leftStickX.get();
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return mapJoystick(amount);
    }

    public double getDriveY() {
        double amount = drive.leftStickY.get();
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return mapJoystick(amount);
    }

    public double getRot() {
        double amount = drive.rightStickX.get();
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return mapJoystick(amount);
    }



    /* Intake */
    public boolean getIntake() {
        return manipulator.leftShoulder.isPressed();
    }

    // Unused
    private boolean intakeLift = true;
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
