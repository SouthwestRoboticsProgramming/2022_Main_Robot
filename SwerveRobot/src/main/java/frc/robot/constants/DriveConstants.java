package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.drive.SwerveModuleInfo;

public class DriveConstants {
    /* Settings */
    public static final double WHEEL_TURN_KP = 0.009;
    public static final double WHEEL_TURN_KI = 0; // Leave this at 0: There is no steady-state error in the system
    public static final double WHEEL_TURN_KD = 0.0001;
    public static final double WHEEL_TURN_KF = 0;

    public static final double WHEEL_DRIVE_KP = 0;
    public static final double WHEEL_DRIVE_KI = 0;
    public static final double WHEEL_DRIVE_KD = 0;
    public static final double WHEEL_DRIVE_KF = 0;

    public static final double GLOBAL_TURN_KP = 0.027;
    public static final double GLOBAL_TURN_KI = 0;
    public static final double GLOBAL_TURN_KD = 0.0022;

    public static final double MAX_VELOCITY = 0.1; // User Defined - Meters per second
    public static final double MAX_ROTATION_SPEED = 1.0; // User Defined - Radians per second

    public static final double SLOW_MODE = 0.5; // How much to multipy the drive by to go slower
    
    public static final Rotation2d WHEEL_TOLERANCE = Rotation2d.fromDegrees(1); // Degrees - How close a wheel can be to the target angle

    /* Hardware */
    public static final String GERALD = "Gerald";

    public static final double WHEEL_DIAMETER = 0.10;
    public static final int TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION = 2048;
    public static final int TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND =
        TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION * 10;
    public static final double SWERVE_MODULE_DRIVE_GEAR_RATIO = 8.14 / 1;

    public static final double DRIVE_SPEED_TO_NATIVE_VELOCITY = 
        TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND *
        SWERVE_MODULE_DRIVE_GEAR_RATIO *
        WHEEL_DIAMETER * Math.PI;

    public static final double WHEEL_SPACING_FRONT_BACK = 0.4699; // Meters
    public static final double WHEEL_SPACING_LEFT_RIGHT = 0.4699; // Meters

    // TODO: Have a global offset for each module then at 90 for each position that it is in
    public static final double OFFSET_1 = -133.945 + 180;
    public static final double OFFSET_2 = -40.078;
    public static final double OFFSET_3 = -258.047 + 180;
    public static final double OFFSET_4 = -2.373;
    public static final boolean CANCODER_DIRECTION = false; // False = Counterclockwise   True = Clockwise
    
    public static final SwerveModuleInfo[] SWERVE_MODULES = {
        new SwerveModuleInfo(1, 3, 0),
        new SwerveModuleInfo(4, 6, 0),
        new SwerveModuleInfo(7, 9, 0),
        new SwerveModuleInfo(10, 12, 0)
    };

    public static final int TURN_PORT_1 = 2;
    public static final int TURN_PORT_2 = 5;
    public static final int TURN_PORT_3 = 8;
    public static final int TURN_PORT_4 = 11;

    public static final double ROBOT_MAX_VELOCITY = 4.11; // Hardware limitation - Meters per second
    public static final double ROBOT_MAX_ROTATION_SPEED = 26.5; // Hardware limitation - Radians per second

    private DriveConstants() {
        throw new AssertionError();
    }
}
