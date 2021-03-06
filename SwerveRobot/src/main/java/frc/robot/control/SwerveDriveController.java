package frc.robot.control;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.drive.SwerveDrive;
import frc.robot.util.ShuffleBoard;
import frc.robot.util.Utils;

import static frc.robot.constants.ControlConstants.*;
import static frc.robot.constants.DriveConstants.*;

public class SwerveDriveController {
    private final SwerveDrive drive;
    private final Input input;
    private final PIDController rotPID;

    private double autoRot;
    private double autoDriveX, autoDriveY;
    private boolean autoControl;

    // Sets initial state of robot (In this case, staying still)
    private ChassisSpeeds speeds = new ChassisSpeeds(0.0, 0.0, 0.0);
    
    public SwerveDriveController() {
        this.drive = Robot.INSTANCE.drive;
        this.input = Robot.INSTANCE.input;
        rotPID = new PIDController(GLOBAL_TURN_KP, GLOBAL_TURN_KI, GLOBAL_TURN_KD);
        rotPID.enableContinuousInput(-180, 180);
        rotPID.setTolerance(2);
        autoControl = false;
    }

    public void swerveInit() {
        drive.zeroGyro();
        drive.update(speeds);
    }
    
    public void update() {
        double maxVelocity = ShuffleBoard.driveSpeed.getDouble(MAX_VELOCITY);
        double maxRotationSpeed = ShuffleBoard.turnSpeed.getDouble(MAX_ROTATION_SPEED);

        double driveX = input.getDriveX();
        double driveY = input.getDriveY();
        double rot = input.getRot();
        Rotation2d currentAngle = drive.getGyroscopeRotation();

        if (autoControl) {
            rot = autoRot;
            driveX = autoDriveX;
            driveY = autoDriveY;
            // System.out.println("Auto");
        } else {
            // System.out.println("no auto");
        }

        double fieldRelativeX = driveX * maxVelocity;
        double fieldRelativeY = driveY * maxVelocity;
        double targetRot = rot * maxRotationSpeed;

        if (input.getSpeedMode()) {
            fieldRelativeX = fieldRelativeX * SLOW_MODE;
            fieldRelativeY = fieldRelativeY * SLOW_MODE;
        }
                            
        // System.out.println(driveX + " " + driveY + " " + rot);
        
        // Convert motion goals to ChassisSpeeds object
        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeX, fieldRelativeY, targetRot, currentAngle);
        //speeds = new ChassisSpeeds(fieldRelativeX,fieldRelativeY,targetRot);
        drive.update(speeds);

        autoControl = false;
    }

    public void turnToTarget(double angleTargetDegrees) {
        double targetRotPercent = rotPID.calculate(drive.getGyroscopeRotation().getDegrees(),angleTargetDegrees);
        autoControl = true;
        this.autoRot = targetRotPercent;
    }
    
    public boolean isAtTarget() {
        return rotPID.atSetpoint();
    }

    public void drive(double x, double y, double rot) {
        autoControl = true;
        autoDriveX = x;
        autoDriveY = y;
        autoRot = rot;
    }
}
