package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import frc.messenger.client.MessageDispatcher;
import frc.messenger.client.MessengerClient;
import frc.robot.command.Command;
import frc.robot.command.SaveShuffleWoodCommand;
import frc.robot.command.auto.AutonomousCommand;
import frc.robot.command.auto.zero_ball.ZeroBallAuto;
import frc.robot.command.climb.ClimberSequence;
import frc.robot.control.Input;
import frc.robot.control.SwerveDriveController;
import frc.robot.drive.SwerveDrive;
import frc.robot.subsystems.CameraTurret;
import frc.robot.subsystems.Cameras;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Localization;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.NeoTester;
import frc.robot.util.ShuffleBoard;
import frc.robot.util.ShuffleWood;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;

import static frc.robot.constants.MessengerConstants.*;

public class Robot extends TimedRobot {
  public static final Robot INSTANCE = new Robot();

  // Subsystems
  public Input input;

  public AHRS gyro;
  public SwerveDrive drive;
  public SwerveDriveController driveController;

  private MessengerClient msg;
  public MessageDispatcher dispatch;

  public Cameras cameras;
  public CameraTurret cameraTurret;
  public Shooter shooter;
  public Intake intake;

  public Climber climber;

  public RobotState state;
  public Localization localization;

  private Command autoCommand;
  
  @Override
  public void robotInit() {
    new ShuffleBoard(); // static init shuffleboard

    state = RobotState.DISABLED;
    Scheduler.get().initState();

    int attempts = 0;
    while (attempts < MESSENGER_MAX_CONNECT_ATTEMPTS && msg == null) {
      try {
        MessengerClient attempt = new MessengerClient(MESSENGER_HOST, MESSENGER_PORT, "RoboRIO", attempts != MESSENGER_MAX_CONNECT_ATTEMPTS - 1);
        msg = attempt;
      } catch (Throwable t) {
        System.err.println("Connect failed, retrying");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {}
      }

      attempts++;
    }
    if (msg == null) {
      throw new IllegalStateException("Messenger is null, this should never happen!");
    }

    ShuffleBoard.messengerConnected.setBoolean(msg.isConnected());
    ShuffleBoard.messengerAttempts.setNumber(attempts);

    dispatch = new MessageDispatcher(msg);

    ShuffleWood.setMessenger(dispatch);

    input = new Input();
    gyro = new AHRS(SPI.Port.kMXP, (byte) 200);

    drive = new SwerveDrive();
    driveController = new SwerveDriveController();
    
    cameras = new Cameras();
    cameraTurret = new CameraTurret(cameras);
    localization = new Localization();
    shooter = new Shooter();
    intake = new Intake();
    climber = new Climber();

    // new NeoTester(); // TODO: Remove before competition
    
    driveController.swerveInit();

    Scheduler.get().scheduleCommand(new SaveShuffleWoodCommand());
  }

  @Override
  public void robotPeriodic()
  {
    msg.read();
    Scheduler.get().update();
  }

  @Override
  public void autonomousInit() {
    state = RobotState.AUTONOMOUS;
    Scheduler.get().initState();

    switch (ShuffleBoard.whichAuto.getString("a")) {
      case "a":
        Scheduler.get().scheduleCommand(autoCommand = new AutonomousCommand());
        break;
    
      default:
        break;
    }
  }

  @Override
  public void autonomousPeriodic() {
    driveController.update();
  }

  @Override
  public void teleopInit() {
    state = RobotState.TELEOP;
    Scheduler.get().initState();
    if (autoCommand != null) {
      Scheduler.get().cancelCommand(autoCommand);
      autoCommand = null;
    }
  }

  @Override
  public void teleopPeriodic() {
    driveController.update();
  }

  @Override
  public void disabledInit() {
    state = RobotState.DISABLED;
    Scheduler.get().initState();
    drive.disable();

    if (autoCommand != null) {
      Scheduler.get().cancelCommand(autoCommand);
      autoCommand = null;
    }
    ShuffleWood.save();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    state = RobotState.TEST;
    Scheduler.get().initState();
    if (autoCommand != null) {
      Scheduler.get().cancelCommand(autoCommand);
      autoCommand = null;
    }
  }

  @Override
  public void testPeriodic() {}

  public RobotState getState() {
    return state;
  }
}
