package frc.robot.control;

import static frc.robot.constants.ControlConstants.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Robot;
import frc.robot.command.climb.AutoClimb;

public class Input {
    private Robot robot;
    private final XboxController drive;
    //private final XboxController manipulator;

    private final XboxController finalManipulator;
    private final XboxController finalDrive;


    private final Joystick joyManipulator = new Joystick(MANIPULATOR_CONTROLLER);
    private final JoystickButton autoClimb = new JoystickButton(joyManipulator, ...);

    public Input(Robot robot) {
        this.robot = robot;
        drive = new XboxController(DRIVE_CONTROLLER);
        //manipulator = new XboxController(11);

        finalManipulator = new XboxController(MANIPULATOR_CONTROLLER);
        finalDrive = new XboxController(DRIVE_CONTROLLER);

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        // // Create some buttons
        autoClimb.whenPressed(new AutoClimb(robot.climberSub.teleLeft, robot.climberSub.teleLeft, robot.climberSub.swingLeft, robot.climberSub.swingRight, robot));
      }

    public double getDriveX() {
        return drive.getLeftStickX();
    }

    public double getDriveY() {
        return drive.getLeftStickY();
    }

    public double getRot() {
        return drive.getRightStickX();
    }

    public boolean getNextclimbStep() {
        return finalManipulator.get...();
    }



    // For testing things before they have final controls
    public boolean testButton() {
        return finalManipulator.getMenuButton();
    }

    public boolean testButton2() {
        return finalManipulator.getWindowButton();
    }

    public double testSwingingArm() {
        return finalManipulator.getRightStickY();
    }



    /* Final Controler Setups */
    public boolean getIntake() {
        return finalManipulator.getLeftShoulderButton();
    }

    private boolean intakePreviousButton = false;
    private boolean intakePrevious = false;

    public boolean getIntakeLift() {
        boolean finalIntake = false;

        /* Get leading edge */
        boolean pressed = finalManipulator.getYButton() && finalManipulator.getYButton() != intakePreviousButton;

        /* If it's pressed, toggle the intake */
        if (pressed) {
            finalIntake = !intakePrevious;
        } else {
            finalIntake = intakePrevious;
        }
        
        intakePrevious = finalIntake;
        intakePreviousButton = finalManipulator.getYButton();

        return finalIntake;
    }

    private boolean shootPrevious = false;
    public boolean getShoot() {
        if (finalManipulator.getAButton() && finalManipulator.getAButton() != shootPrevious) {
            shootPrevious = finalManipulator.getAButton();
            return true;
        } else {
            shootPrevious = finalManipulator.getAButton();
            return false; 
        }
    }

    public boolean getAim() {
        return finalDrive.getRightShoulderButton() || finalManipulator.getRightShoulderButton();
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
}
