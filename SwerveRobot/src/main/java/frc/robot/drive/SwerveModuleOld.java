package frc.robot.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.MathUtil;
import frc.robot.Scheduler;
import frc.robot.command.Command;
import frc.robot.util.ShuffleBoard;
import frc.robot.util.Utils;

import static frc.robot.constants.DriveConstants.*;


public class SwerveModuleOld {

    private final WPI_TalonFX driveMotor;
    private final WPI_TalonSRX turnMotor;
    private final CANCoder canCoder;
    private final double canOffset;
    private final PIDController turnPID;

    public SwerveModuleOld(int drivePort, int turnPort, int canPort, double cancoderOffset) {
        driveMotor = new WPI_TalonFX(drivePort, GERALD);
        turnMotor = new WPI_TalonSRX(turnPort);
        canCoder = new CANCoder(canPort, GERALD);
        canOffset = cancoderOffset;

        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        {
            driveConfig.neutralDeadband = 0.001;
            driveConfig.slot0.kF = WHEEL_DRIVE_KF;
            driveConfig.slot0.kP = WHEEL_DRIVE_KP;
            driveConfig.slot0.kI = WHEEL_DRIVE_KI;
            driveConfig.slot0.kD = WHEEL_DRIVE_KD;
            driveConfig.slot0.closedLoopPeakOutput = 1;
            driveConfig.openloopRamp = 0.5;
            driveConfig.closedloopRamp = 0.5;
        }
        driveMotor.configAllSettings(driveConfig);
        driveMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        TalonSRXConfiguration turnConfig = new TalonSRXConfiguration();
        turnMotor.configAllSettings(turnConfig);
        driveMotor.setNeutralMode(NeutralMode.Brake);
        driveMotor.setSelectedSensorPosition(0, 0, 30);
        driveMotor.stopMotor();

        turnMotor.setNeutralMode(NeutralMode.Brake);
        turnMotor.setSelectedSensorPosition(0, 0, 30);
        turnMotor.stopMotor();

        CANCoderConfiguration canConfig = new CANCoderConfiguration();
        canConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        canConfig.magnetOffsetDegrees = canOffset;
        canConfig.sensorDirection = CANCODER_DIRECTION;

        canCoder.configAllSettings(canConfig);

        turnPID = new PIDController(WHEEL_TURN_KP, WHEEL_TURN_KI, WHEEL_TURN_KD);
        turnPID.enableContinuousInput(-90, 90);
        turnPID.setTolerance(WHEEL_TOLERANCE.getDegrees());

        Scheduler.get().scheduleCommand(new Command() {
            @Override
            public boolean run() {
                System.out.println("Do the dew");

                // turnPID.setPID(
                //     ShuffleBoard.wheelTurnKP.getDouble(WHEEL_TURN_KP),
                //     ShuffleBoard.wheelTurnKI.getDouble(WHEEL_TURN_KI),
                //     ShuffleBoard.wheelTurnKD.getDouble(WHEEL_TURN_KD)
                // );

                driveMotor.config_kP(0, ShuffleBoard.wheelDriveKP.getDouble(WHEEL_DRIVE_KP));
                driveMotor.config_kI(0, ShuffleBoard.wheelDriveKI.getDouble(WHEEL_DRIVE_KI));
                driveMotor.config_kD(0, ShuffleBoard.wheelDriveKD.getDouble(WHEEL_DRIVE_KD));
                driveMotor.config_kF(0, ShuffleBoard.wheelDriveKF.getDouble(WHEEL_DRIVE_KF));

                return false;
            }

            @Override
            public int getInterval() {
                return 50;
            }
        });
    }

    public void update(SwerveModuleState swerveModuleState) {
        Rotation2d canRotation = new Rotation2d(Math.toRadians(canCoder.getAbsolutePosition()));
        Rotation2d currentAngle = new Rotation2d(Math.toRadians(Utils.fixCurrentAngle(canCoder.getAbsolutePosition())));
        SwerveModuleState moduleState = SwerveModuleState.optimize(swerveModuleState, canRotation);
        Rotation2d targetAngle = moduleState.angle;

        // Turn to target angle
        double turnAmount = turnPID.calculate(currentAngle.getDegrees(),targetAngle.getDegrees());
        turnAmount = MathUtil.clamp(turnAmount,-1.0,1.0);

        // Spin the motors
        if (!turnPID.atSetpoint())
            turnMotor.set(ControlMode.PercentOutput, turnAmount); 
        else
            turnMotor.set(ControlMode.PercentOutput, 0);

        // go velocity
        driveMotor.set(TalonFXControlMode.Velocity, moduleState.speedMetersPerSecond * DRIVE_SPEED_TO_NATIVE_VELOCITY);
    }

    public double getCanRotation() {
        return canCoder.getAbsolutePosition();
    }

    public void disable() {
        turnPID.reset();
    }
}
