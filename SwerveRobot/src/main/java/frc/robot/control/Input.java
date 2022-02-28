package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Robot;
import frc.robot.command.climb.AutoClimb;

public class Input {
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
        amount = drive.leftStickX.get();
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return mapJoystick(amount);
    }

    public double getDriveY() {
        amount = drive.leftStickY.get();
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return mapJoystick(amount);
    }

    public double getRot() {
        amount = drive.rightStickX.get();
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

        return intakeLift;
    }

    /* Shooter */
    public boolean getShoot() {
        return manipulator.a.leadingEdge;
    }

    // Unused
    public boolean getAim() {
        return finalDrive.getRightShoulderButton() || finalManipulator.getRightShoulderButton();
    }

    public int getShootDistance() {
        if (manipulaotr.dpadUp.isPressed()) {return 0}
        if (manipulaotr.dpadDown.isPressed()) {return 2}
        return 1;
    }


    /* Climber */
    public double getClimbTele() {
        if (Math.abs(finalManipulator.getLeftStickY()) > JOYSTICK_DEAD_ZONE){
            return finalManipulator.getLeftStickY();
        } else {
            return 0;
        }
    }

    public boolean getClimbNextStep() {
        //return finalManipulator.getLeftShoulderButton() && finalManipulator.getRightShoulderButton();
        return finalManipulator.getDpadUp();
    }


    /* Tools */
    private double mapJoystick(double amount) {
        /*
        if (amount > 0) {
            return Utils.map(driveX, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        }
        return -Utils.map(-amount, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        */
        return (amount / Math.abs(amount)) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE, 1, 0, 1);
    }
}
