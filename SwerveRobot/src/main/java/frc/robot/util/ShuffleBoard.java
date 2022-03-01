package frc.robot.util;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import static frc.robot.constants.DriveConstants.*;
import static frc.robot.constants.ShooterConstants.*;
import static frc.robot.constants.IntakeConstants.*;
import static frc.robot.constants.ClimberConstants.*;

public class ShuffleBoard {
    public static ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
        private static ShuffleboardLayout drive = driveTab.getLayout("drive", BuiltInLayouts.kList);
            public static NetworkTableEntry wheelTurnKP = drive.addPersistent("Wheel Turn KP", WHEEL_TURN_KP).getEntry();
            public static NetworkTableEntry wheelTurnKI = drive.addPersistent("Wheel Turn KI", WHEEL_TURN_KI).getEntry();
            public static NetworkTableEntry wheelTurnKD = drive.addPersistent("Wheel Turn KD", WHEEL_TURN_KD).getEntry();
            public static NetworkTableEntry wheelDriveScale = drive.addPersistent("Wheel Drive Scale", 1).getEntry();
    
    public static ShuffleboardTab tuneTab = Shuffleboard.getTab("Tune");
        private static ShuffleboardLayout tune = tuneTab.getLayout("Tune", BuiltInLayouts.kList);
            public static NetworkTableEntry hoodPosition = tune.addPersistent("Hood Position (0-4)", 0).getEntry();
            public static NetworkTableEntry shooterFlywheelVelocity = tune.addPersistent("Shooter Flywheel Velocity", SHOOTER_IDLE_VELOCITY).getEntry();

        private static ShuffleboardLayout flywheelTune = tuneTab.getLayout("Flywheel", BuiltInLayouts.kList);
            public static NetworkTableEntry flywheelKP = flywheelTune.addPersistent("KP", FLYWHEEL_KP).getEntry();
            public static NetworkTableEntry flywheelKI = flywheelTune.addPersistent("KI", FLYWHEEL_KI).getEntry();
            public static NetworkTableEntry flywheelKD = flywheelTune.addPersistent("KD", FLYWHEEL_KD).getEntry();

        private static ShuffleboardLayout indexTune = tuneTab.getLayout("Index", BuiltInLayouts.kList);
            public static NetworkTableEntry indexKP = indexTune.addPersistent("KP", INDEX_KP).getEntry();
            public static NetworkTableEntry indexKI = indexTune.addPersistent("KI", INDEX_KI).getEntry();
            public static NetworkTableEntry indexKD = indexTune.addPersistent("KD", INDEX_KD).getEntry();
            public static NetworkTableEntry indexSpeed = indexTune.addPersistent("Speed", INDEX_SPEED).getEntry();

        private static ShuffleboardLayout hoodTune = tuneTab.getLayout("Hood", BuiltInLayouts.kList);
            public static NetworkTableEntry hoodKP = hoodTune.addPersistent("KP", HOOD_KP).getEntry();
            public static NetworkTableEntry hoodKI = hoodTune.addPersistent("KI", HOOD_KI).getEntry();
            public static NetworkTableEntry hoodKD = hoodTune.addPersistent("KD", HOOD_KD).getEntry();

        private static ShuffleboardLayout intakeTune = tuneTab.getLayout("Intake", BuiltInLayouts.kList);
            public static NetworkTableEntry intakeKP = intakeTune.addPersistent("KP", INTAKE_KP).getEntry();
            public static NetworkTableEntry intakeKI = intakeTune.addPersistent("KI", INTAKE_KI).getEntry();
            public static NetworkTableEntry intakeKD = intakeTune.addPersistent("KD", INTAKE_KD).getEntry();
            public static NetworkTableEntry intakeFullVelocity = intakeTune.addPersistent("Full Velocity", INTAKE_FULL_VELOCITY).getEntry();
            public static NetworkTableEntry intakeNeutralVelocity = intakeTune.addPersistent("Neutral Velocity", INTAKE_NEUTRAL_VELOCITY).getEntry();
        
        private static ShuffleboardLayout telescopeTune = tuneTab.getLayout("Telescope", BuiltInLayouts.kList);
            public static NetworkTableEntry climberTelescopeKP = telescopeTune.addPersistent("KP", CLIMBER_TELE_MOTOR_KP).getEntry();
            public static NetworkTableEntry climberTelescopeKI = telescopeTune.addPersistent("KI", CLIMBER_TELE_MOTOR_KI).getEntry();
            public static NetworkTableEntry climberTelescopeKD = telescopeTune.addPersistent("KD", CLIMBER_TELE_MOTOR_KD).getEntry();

        private static ShuffleboardLayout climbTune = tuneTab.getLayout("climbTune", BuiltInLayouts.kList);
            public static NetworkTableEntry climbTuneTeleTolerence = climbTune.addPersistent("TeleTolerence", 0).getEntry();
            public static NetworkTableEntry climbTuneSwingTolerence = climbTune.addPersistent("SwingTolerence", 0).getEntry();
            public static NetworkTableEntry climbTune1TeleHeight = climbTune.addPersistent("1TeleExtendHeight", 0).getEntry();
            public static NetworkTableEntry climbTune1SwingAngle = climbTune.addPersistent("1SwingAngle", 0).getEntry();
            public static NetworkTableEntry climbTune2TeleHeight = climbTune.addPersistent("2TeleRetractHeight", 0).getEntry();
            public static NetworkTableEntry climbTune3SwingAngle = climbTune.addPersistent("3SwingAngle", 0).getEntry();
            public static NetworkTableEntry climbTune4TeleHeight = climbTune.addPersistent("4Tele2-3Length", 0).getEntry();
            public static NetworkTableEntry climbTune5SwingAngle = climbTune.addPersistent("5SwingCompressonAngle", 0).getEntry();

        private static ShuffleboardLayout autoSelect = tuneTab.getLayout("Auto Select", BuiltInLayouts.kList);
            public static NetworkTableEntry whichAuto = autoSelect.addPersistent("Which Auto", "a").getEntry();

        private static ShuffleboardLayout velocities = tuneTab.getLayout("Shooter Velocities", BuiltInLayouts.kList);
            public static NetworkTableEntry closeVelocity = velocities.addPersistent("Close Velocity", CLOSE_SPEED).getEntry();
            public static NetworkTableEntry mediumVelocity = velocities.addPersistent("Medium Velocity", LINE_SPEED).getEntry();
            public static NetworkTableEntry farVelocity = velocities.addPersistent("Far Velocity", LAUNCHPAD_SPEED).getEntry();
        // close, medium, far

}
