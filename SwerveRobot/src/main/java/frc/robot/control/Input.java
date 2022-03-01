package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Subsystem;
import frc.robot.util.Utils;

public class Input extends Subsystem {
    private final XboxController drive;
    private final XboxController manipulator;

    private final Joystick tempManipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER);

        tempManipulator = new Joystick(1);
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

    public int getShootDistance() {
        if (manipulator.dpadUp.isPressed()) return 0;
        if (manipulator.dpadDown.isPressed()) return 2;
        return 1;
    }

    /* Climber */
    public double getTeleManual() {
        return mapJoystick(tempManipulator.getRawAxis(1));
    }

    public double getSwingManual() {
        return mapJoystick(tempManipulator.getRawAxis(5));
    }

    public boolean getNextStep() {
        return new JoystickButton(tempManipulator, 6).get();
    }

    public boolean getPreviousStep() {
        return new JoystickButton(tempManipulator, 5).get();
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
}
