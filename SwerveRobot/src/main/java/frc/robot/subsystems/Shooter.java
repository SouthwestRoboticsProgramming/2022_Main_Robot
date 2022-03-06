package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Robot;
import frc.robot.Scheduler;
import frc.robot.command.shooter.IndexBall;
import frc.robot.command.shooter.Shoot;
import frc.robot.constants.DriveConstants;
import frc.robot.control.Input;
import frc.robot.util.ShuffleBoard;

import static frc.robot.constants.ShooterConstants.*;

public class Shooter extends Subsystem {
  private final Input input;
  private final TalonFX flywheel;
  private final TalonFX index;
  private final TalonSRX hood;
  private final DigitalInput hoodLimit;

  private boolean calibratingHood = true;
  public double manualDistance = -1;

  public Shooter() {
    this.input = Robot.INSTANCE.input;
    
    flywheel = new TalonFX(FLYWHEEL_MOTOR_ID);
    index = new TalonFX(INDEX_MOTOR_ID, DriveConstants.GERALD);
    hood = new TalonSRX(HOOD_MOTOR_ID);

    index.setInverted(true);
    flywheel.setInverted(true);
    hood.setInverted(true);

    hoodLimit = new DigitalInput(HOOD_LIMIT_CHANNEL);

    TalonFXConfiguration flywheelConfig = new TalonFXConfiguration();
    flywheelConfig.neutralDeadband = 0.001;
    flywheelConfig.slot0.kF = FLYWHEEL_KF;
    flywheelConfig.slot0.kP = FLYWHEEL_KP;
    flywheelConfig.slot0.kI = FLYWHEEL_KI;
    flywheelConfig.slot0.kD = FLYWHEEL_KD;
    flywheelConfig.slot0.closedLoopPeakOutput = 1;
    flywheelConfig.openloopRamp = 0.5;
    flywheelConfig.closedloopRamp = 0.5;
    flywheel.configAllSettings(flywheelConfig);
    flywheel.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    TalonFXConfiguration indexConfig = new TalonFXConfiguration();
    indexConfig.neutralDeadband = 0.001;
    indexConfig.slot0.kF = INDEX_KF;
    indexConfig.slot0.kP = INDEX_KP;
    indexConfig.slot0.kI = INDEX_KI;
    indexConfig.slot0.kD = INDEX_KD;
    indexConfig.slot0.closedLoopPeakOutput = 1;
    indexConfig.openloopRamp = 0.5;
    indexConfig.closedloopRamp = 0.5;
    index.configAllSettings(indexConfig);
    index.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    TalonSRXConfiguration hoodConfig = new TalonSRXConfiguration();
    hoodConfig.neutralDeadband = 0.001;    
    indexConfig.slot0.kF = HOOD_KF;
    hoodConfig.slot0.kP = HOOD_KP;
    hoodConfig.slot0.kI = HOOD_KI;
    hoodConfig.slot0.kD = HOOD_KD;
    hoodConfig.slot0.closedLoopPeakOutput = 1;
    hoodConfig.openloopRamp = 0.5;
    hoodConfig.closedloopRamp = 0.5;
    hood.configAllSettings(hoodConfig);
    hood.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    hood.setSensorPhase(true);
  }

  public void shoot() {
    Scheduler.get().scheduleCommand(new Shoot(index));
  }

  public Shoot makeShootCommand() {
    return new Shoot(index);
  }

  public void shootManualDistance() {
    Scheduler.get().scheduleCommand(new Shoot(index));
    // TODO: Add distance to shoot command
  }

  private double calculateSpeed(int hoodAngle, int distance /*double distance */) {
    switch (distance) {
      case 0:
        return ShuffleBoard.closeVelocity.getDouble(CLOSE_SPEED);
      case 1:
        return ShuffleBoard.mediumVelocity.getDouble(LINE_SPEED);
      case 2:
        return ShuffleBoard.farVelocity.getDouble(LAUNCHPAD_SPEED);
      case 3:
        return ShuffleBoard.autoVelocity.getDouble(AUTO_SPEED);
      default:
        return SHOOTER_IDLE_VELOCITY;
    }
  }

  private int calculateHood(int distance) {
    switch (distance) {
      case 0:
        return (int) ShuffleBoard.closeHood.getDouble(CLOSE_HOOD_POS);
      case 1:
        return (int) ShuffleBoard.mediumHood.getDouble(MEDIUM_HOOD_POS);
      case 2:
        return (int) ShuffleBoard.farHood.getDouble(FAR_HOOD_POS);
      case 3:
        return (int) ShuffleBoard.autoHood.getDouble(AUTO_HOOD_POS);
    
      default:
        return 0;
    }
  }

  double lastHoodAngle = 0;

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }
  
  @Override
  public void teleopPeriodic() {
    // double distance = cameraTurret.getDistance;

    // double angle = cameraTurret.getAngle;
    
    /* Hood control */
    //double hoodAngle = Utils.clamp(ShuffleBoard.hoodPosition.getDouble(0), 0, 4);

    double distance = (double) input.getShootDistance();

    if (manualDistance >= 0)
      distance = manualDistance;

    double hoodAngle = (double) calculateHood((int)distance);
    if (hoodAngle == 0 && lastHoodAngle != 0) {
      calibratingHood = true;
    }
    lastHoodAngle = hoodAngle;
    //System.out.println("Hood angle is " + hoodAngle);
    double targetHood = (hoodAngle / 3.0 * ROTS_PER_MIN_MAX * TICKS_PER_ROT) + 20;
    
    if (calibratingHood) {
      hood.set(ControlMode.PercentOutput, -0.2);
      // System.out.println("Calibrating");

      if (hoodLimit.get()) {
        // System.out.println("Calibrated!");
        calibratingHood = false;

        ShuffleBoard.hoodPosition.setDouble(0);
        hood.setSelectedSensorPosition(0);
      }
    } else {
      hood.set(ControlMode.Position, targetHood);
    }

    // System.out.println(hood.getSelectedSensorPosition());

    // System.out.printf("Current: %3.3f Target: %3.3f %n", hood.getSelectedSensorPosition(), targetHood);

    // if (true) {
    //   flywheel.set(ControlMode.Velocity, ShuffleBoard.shooterFlywheelVelocity.getDouble(SHOOTER_IDLE_VELOCITY)/*calculateSpeed(distance, hoodAngle)*/);
    // } else {
    //   flywheel.set(ControlMode.Velocity, SHOOTER_IDLE_VELOCITY);
    // }
    

    flywheel.set(ControlMode.Velocity, calculateSpeed(0,(int)distance));
    if (input.getShoot()) {
      shoot();
    }
  }
}
