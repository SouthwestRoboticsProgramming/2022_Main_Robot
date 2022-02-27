package frc.robot.control;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.drive.SwerveDrive;
import frc.robot.util.ShuffleWood;
import frc.robot.util.Utils;

import static frc.robot.constants.ControlConstants.*;
import static frc.robot.constants.DriveConstants.*;

public class SwerveDriveController {
    private final SwerveDrive drive;
    private final Input input;
    private final PIDController rotPID;

    private double targetAngle = 0;
    private double autoDriveX, autoDriveY;
    private boolean autoControl;

    // Sets initial state of robot (In this case, staying still)
    private ChassisSpeeds speeds = new ChassisSpeeds(0.0, 0.0, 0.0);
    
    public SwerveDriveController(SwerveDrive drive, Input input) {
        this.drive = drive;
        this.input = input;
        rotPID = new PIDController(GLOBAL_TURN_KP, GLOBAL_TURN_KI, GLOBAL_TURN_KD);
        rotPID.enableContinuousInput(-180, 180);
        rotPID.setTolerance(2);
        autoControl = false;
    }

    public void swerveInit(){
        drive.zeroGyro();
        drive.update(speeds);
    }

    boolean lastRotDeadzone = false;
    double rotTarget = 0;

    public void update() {
        // Get inputs
        double driveX = input.getDriveX();
        double driveY = input.getDriveY();
        double rot = input.getRot();
        Rotation2d currentAngle = drive.getGyroscopeRotation();

        // Clamp to zero when in deadzone
        boolean xInDeadzone = Math.abs(driveX) < JOYSTICK_DEAD_ZONE;
        boolean yInDeadzone = Math.abs(driveY) < JOYSTICK_DEAD_ZONE;
        boolean rotInDeadzone = Math.abs(rot) < JOYSTICK_DEAD_ZONE;
        if (xInDeadzone) driveX = 0;
        if (yInDeadzone) driveY = 0;
        if (rotInDeadzone) rot = 0;

        // Smoothly transition when leaving deadzone
        if (driveX > 0) driveX = Utils.map(driveX, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        else if (driveX < 0) driveX = -Utils.map(-driveX, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        if (driveY > 0) driveY = Utils.map(driveY, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        else if (driveY < 0) driveY = -Utils.map(-driveY, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        if (rot > 0) rot = Utils.map(rot, JOYSTICK_DEAD_ZONE, 1, 0, 1);
        else if (rot < 0) rot = -Utils.map(-rot, JOYSTICK_DEAD_ZONE, 1, 0, 1);

        // Eliminate rotation drift
        // {
        //     // If we just entered the deadzone, record the current orientation
        //     if (rotInDeadzone && !lastRotDeadzone) {
        //         rotTarget = currentAngle.getDegrees();
        //     }
        //     lastRotDeadzone = rotInDeadzone;

        //     // If we are in the deadzone, target the recorded orientation to ensure it doesn't rotate
        //     if (rotInDeadzone) {
        //         // Normalize things
        //         double current = Utils.normalizeAngleDegrees(currentAngle.getDegrees());
        //         double target = Utils.normalizeAngleDegrees(rotTarget);

        //         // if (rotPID.atSetpoint())
        //         //     rot = 0;+
        //         // else
        //             rot = rotPID.calculate(current, target);
        //         rot = Utils.clamp(rot, -1, 1);

        //         System.out.println(current + " -> " + targetAngle + " (" + rot + ")");
        //     }
        // }

        // Get actual motion speeds
        double chassisDriveX = driveX * MAX_VELOCITY;
        double chassisDriveY = driveY * MAX_VELOCITY;
        double chassisRot = rot * MAX_ROTATION_SPEED;

        // Calculate speeds
        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(chassisDriveX, chassisDriveY, chassisRot, currentAngle);

        // Tell the drive assembly to do it
        drive.update(speeds);
    }
    
    // public void update() {
    //     double driveX = input.getDriveX();
    //     double driveY = input.getDriveY();
    //     double rot = input.getRot();
    //     Rotation2d currentAngle = drive.getGyroscopeRotation();

    //     // ShuffleWood.show("currentAngle", currentAngle);
        
    //     boolean isTargetingRot = false;

    //     if (Math.abs(driveX) < JOYSTICK_DEAD_ZONE) {
    //         driveX = 0;
    //     }
    //     if (Math.abs(driveY) < JOYSTICK_DEAD_ZONE) {
    //         driveY = 0;
    //     }
    //     if (Math.abs(rot) < JOYSTICK_DEAD_ZONE) {
    //         rot = 0;
    //         // rot = rotPID.calculate(drive.getGyroscopeRotation().getDegrees(), targetAngle);
    //         // isTargetingRot = true;
    //     }
        
    //     // Eliminate deadzone jump

    //     if (driveX > 0) {
    //         driveX = Utils.map(driveX, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     } else if (driveX < 0){
    //         driveX = -Utils.map(-driveX, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     }
    //     if (driveY > 0) {
    //         driveY = Utils.map(driveY, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     } else if (driveY <0){
    //         driveY = -Utils.map(-driveY, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     }
    //     if (rot > 0) {
    //         rot = Utils.map(rot, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     } else if (rot < 0) {
    //         rot = -Utils.map(-rot, JOYSTICK_DEAD_ZONE, 1, 0, 1);
    //     }

    //     // if (autoControl) {
    //     //     isTargetingRot = true;
    //     //     driveX = autoDriveX;
    //     //     driveY = autoDriveY;
    //     //     System.out.println("Auto");
    //     // } else {
            
    //     // }

    //     // if (isTargetingRot) {
    //     //     System.out.println("Targeting rot");
    //     //     rot = rotPID.calculate(drive.getGyroscopeRotation().getDegrees(), targetAngle);
    //     // } else {
    //     //     System.out.println("Not targeting rot");
    //     // }

    //     double fieldRelativeX = driveX * MAX_VELOCITY;
    //     double fieldRelativeY = driveY * MAX_VELOCITY;
    //     double targetRot = rot * MAX_ROTATION_SPEED;
                            
    //     // System.out.println(driveX + " " + driveY + " " + rot);
        
    //     // Convert motion goals to ChassisSpeeds object
    //     speeds = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeX, fieldRelativeY, targetRot, currentAngle);
    //     //speeds = new ChassisSpeeds(fieldRelativeX,fieldRelativeY,targetRot);
    //     drive.update(speeds);

    //     autoControl = false;
    // }

    public void turnToTarget(double angleTargetDegrees) {
        autoControl = true;
        targetAngle = angleTargetDegrees;
    }
    
    public boolean isAtTarget() {
        return rotPID.atSetpoint();
    }

    public void drive(double x, double y, double rot) {
        autoControl = true;
        autoDriveX = x;
        autoDriveY = y;
        // FIXME: Rotation
    }
}
