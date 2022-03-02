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
    public static ShuffleboardTab configTab = Shuffleboard.getTab("Config");
        private static ShuffleboardLayout valueDisplay = configTab.getLayout("Value Display", BuiltInLayouts.kList);
            public static NetworkTableEntry valueA = valueDisplay.add("valueA", "null").getEntry();
            public static NetworkTableEntry valueB = valueDisplay.add("valueB", "null").getEntry();
            public static NetworkTableEntry valueC = valueDisplay.add("valueC", "null").getEntry();
            public static NetworkTableEntry valueD = valueDisplay.add("valueD", "null").getEntry();
            public static NetworkTableEntry valueE = valueDisplay.add("valueE", "null").getEntry();

        private static ShuffleboardLayout autoSelect = configTab.getLayout("Auto Select", BuiltInLayouts.kList);
            public static NetworkTableEntry whichAuto = autoSelect.addPersistent("Which Auto", "a").getEntry();

    public static ShuffleboardTab tuneTab = Shuffleboard.getTab("Tune");
        public static ShuffleboardLayout driveGroup = tuneTab.getLayout("Drive", "Subsystem Layout");
            private static ShuffleboardLayout turn = driveGroup.getLayout("Turn", BuiltInLayouts.kList);
                public static NetworkTableEntry wheelTurnKP = turn.addPersistent("Wheel Turn KP", WHEEL_TURN_KP).getEntry();
                public static NetworkTableEntry wheelTurnKI = turn.addPersistent("Wheel Turn KI", WHEEL_TURN_KI).getEntry();
                public static NetworkTableEntry wheelTurnKD = turn.addPersistent("Wheel Turn KD", WHEEL_TURN_KD).getEntry();
            private static ShuffleboardLayout drive = driveGroup.getLayout("Drive", BuiltInLayouts.kList);
                public static NetworkTableEntry wheelDriveScale = drive.addPersistent("Wheel Drive Scale", 1).getEntry();
                public static NetworkTableEntry wheelDriveKP = drive.addPersistent("Wheel Drive KP", WHEEL_DRIVE_KP).getEntry();
                public static NetworkTableEntry wheelDriveKI = drive.addPersistent("Wheel Drive KI", WHEEL_DRIVE_KI).getEntry();
                public static NetworkTableEntry wheelDriveKD = drive.addPersistent("Wheel Drive KD", WHEEL_DRIVE_KD).getEntry();
                public static NetworkTableEntry wheelDriveKF = drive.addPersistent("Wheel Drive KF", WHEEL_DRIVE_KF).getEntry();

        private static ShuffleboardLayout shooterGroup = tuneTab.getLayout("Shooter", "Subsystem Layout");
            public static NetworkTableEntry hoodPosition = shooterGroup.addPersistent("Hood Position (0-4)", 0).getEntry();

            private static ShuffleboardLayout flywheelTune = shooterGroup.getLayout("Flywheel", BuiltInLayouts.kList);
                public static NetworkTableEntry flywheelKP = flywheelTune.addPersistent("KP", FLYWHEEL_KP).getEntry();
                public static NetworkTableEntry flywheelKI = flywheelTune.addPersistent("KI", FLYWHEEL_KI).getEntry();
                public static NetworkTableEntry flywheelKD = flywheelTune.addPersistent("KD", FLYWHEEL_KD).getEntry();
                public static NetworkTableEntry shooterFlywheelVelocity = flywheelTune.addPersistent("Shooter Flywheel Velocity", SHOOTER_IDLE_VELOCITY).getEntry();
            
            private static ShuffleboardLayout indexTune = shooterGroup.getLayout("Index", BuiltInLayouts.kList);
                public static NetworkTableEntry indexKP = indexTune.addPersistent("KP", INDEX_KP).getEntry();
                public static NetworkTableEntry indexKI = indexTune.addPersistent("KI", INDEX_KI).getEntry();
                public static NetworkTableEntry indexKD = indexTune.addPersistent("KD", INDEX_KD).getEntry();
                public static NetworkTableEntry indexSpeed = indexTune.addPersistent("Speed", INDEX_SPEED).getEntry();

            private static ShuffleboardLayout hoodTune = shooterGroup.getLayout("Hood", BuiltInLayouts.kList);
                public static NetworkTableEntry hoodKP = hoodTune.addPersistent("KP", HOOD_KP).getEntry();
                public static NetworkTableEntry hoodKI = hoodTune.addPersistent("KI", HOOD_KI).getEntry();
                public static NetworkTableEntry hoodKD = hoodTune.addPersistent("KD", HOOD_KD).getEntry();

            private static ShuffleboardLayout velocities = shooterGroup.getLayout("Shooter Velocities", BuiltInLayouts.kList);
                public static NetworkTableEntry closeVelocity = velocities.addPersistent("Close Velocity", CLOSE_SPEED).getEntry();
                public static NetworkTableEntry mediumVelocity = velocities.addPersistent("Medium Velocity", LINE_SPEED).getEntry();
                public static NetworkTableEntry farVelocity = velocities.addPersistent("Far Velocity", LAUNCHPAD_SPEED).getEntry();

        private static ShuffleboardLayout climberGroup = tuneTab.getLayout("Climber", "Subsystem Layout");
            private static ShuffleboardLayout climbTune = climberGroup.getLayout("Steps", BuiltInLayouts.kList);
                public static NetworkTableEntry climbTuneTeleTolerence = climbTune.addPersistent("TeleTolerence", 0).getEntry();
                public static NetworkTableEntry climbTuneSwingTolerence = climbTune.addPersistent("SwingTolerence", 0).getEntry();
                public static NetworkTableEntry climbTune1TeleHeight = climbTune.addPersistent("1TeleExtendHeight", 0).getEntry();
                public static NetworkTableEntry climbTune1SwingAngle = climbTune.addPersistent("1SwingAngle", 0).getEntry();
                public static NetworkTableEntry climbTune2TeleHeight = climbTune.addPersistent("2TeleRetractHeight", 0).getEntry();
                public static NetworkTableEntry climbTune3SwingHandoffAngle = climbTune.addPersistent("3SwingHandoffAngle", 0).getEntry();
                public static NetworkTableEntry climbTune4TeleHeight = climbTune.addPersistent("4ReleaseHeight", 0).getEntry();
                public static NetworkTableEntry climbTune5SwingFinishAngle = climbTune.addPersistent("5FinishSwingAngle", 0).getEntry();
                public static NetworkTableEntry climbTune6TeleHeight = climbTune.addPersistent("6Tele2-3Length", 0).getEntry();
                public static NetworkTableEntry climbTune7SwingAngle = climbTune.addPersistent("7SwingCompressonAngle", 0).getEntry();
            
            private static ShuffleboardLayout telescopeTune = climberGroup.getLayout("Telescope", BuiltInLayouts.kList);
                public static NetworkTableEntry climberTelescopeKP = telescopeTune.addPersistent("KP", CLIMBER_TELE_MOTOR_KP).getEntry();
                public static NetworkTableEntry climberTelescopeKI = telescopeTune.addPersistent("KI", CLIMBER_TELE_MOTOR_KI).getEntry();
                public static NetworkTableEntry climberTelescopeKD = telescopeTune.addPersistent("KD", CLIMBER_TELE_MOTOR_KD).getEntry();
                public static NetworkTableEntry climberTelescopeLoadedKP = telescopeTune.addPersistent("LoadedKP", CLIMBER_TELE_MOTOR_KP).getEntry();
                public static NetworkTableEntry climberTelescopeLoadedKI = telescopeTune.addPersistent("LoadedKI", CLIMBER_TELE_MOTOR_KI).getEntry();
                public static NetworkTableEntry climberTelescopeLoadedKD = telescopeTune.addPersistent("LoadedKD", CLIMBER_TELE_MOTOR_KD).getEntry();

            private static ShuffleboardLayout swingTune = climberGroup.getLayout("Swing", BuiltInLayouts.kList);
                public static NetworkTableEntry climberSwingKP = swingTune.addPersistent("KP", CLIMBER_SWING_MOTOR_KP).getEntry();
                public static NetworkTableEntry climberSwingKI = swingTune.addPersistent("KI", CLIMBER_SWING_MOTOR_KI).getEntry();
                public static NetworkTableEntry climberSwingKD = swingTune.addPersistent("KD", CLIMBER_SWING_MOTOR_KD).getEntry();
                public static NetworkTableEntry climberSwingLoadedKP = swingTune.addPersistent("LoadedKP", CLIMBER_SWING_MOTOR_KP).getEntry();
                public static NetworkTableEntry climberSwingLoadedKI = swingTune.addPersistent("LoadedKI", CLIMBER_SWING_MOTOR_KI).getEntry();
                public static NetworkTableEntry climberSwingLoadedKD = swingTune.addPersistent("LoadedKD", CLIMBER_SWING_MOTOR_KD).getEntry();

        private static ShuffleboardLayout intakeTune = tuneTab.getLayout("Intake", BuiltInLayouts.kList);
            public static NetworkTableEntry intakeKP = intakeTune.addPersistent("KP", INTAKE_KP).getEntry();
            public static NetworkTableEntry intakeKI = intakeTune.addPersistent("KI", INTAKE_KI).getEntry();
            public static NetworkTableEntry intakeKD = intakeTune.addPersistent("KD", INTAKE_KD).getEntry();
            public static NetworkTableEntry intakeFullVelocity = intakeTune.addPersistent("Full Velocity", INTAKE_FULL_VELOCITY).getEntry();
            public static NetworkTableEntry intakeNeutralVelocity = intakeTune.addPersistent("Neutral Velocity", INTAKE_NEUTRAL_VELOCITY).getEntry();
}
