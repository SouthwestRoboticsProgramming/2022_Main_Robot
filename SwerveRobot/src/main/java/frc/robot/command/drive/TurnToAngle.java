package frc.robot.command.drive;

import frc.robot.Robot;
import frc.robot.command.Command;
import frc.robot.control.SwerveDriveController;

public class TurnToAngle implements Command {
  private final double angle;

  public TurnToAngle(double angle) {
    this.angle = angle;
  }

  @Override
  public boolean run() {
    SwerveDriveController drive = Robot.INSTANCE.driveController;

    drive.turnToTarget(angle);
    return drive.isAtTarget();
  }
}
