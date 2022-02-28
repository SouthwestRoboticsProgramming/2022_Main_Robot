package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Robot;
import frc.robot.drive.SwerveDrive;

// https://www.desmos.com/calculator/w5x76wa3yd

/* ALL ANGLES IN DEGREES {-180,180} UNLESS NOTED  */
public class Localization extends Subsystem {
  private final SwerveDrive drive;

  public Localization() {
    drive = Robot.INSTANCE.drive;
  }

  public double getX() {
    return drive.getOdometry().getX();
  }

  public double getY() {
    return drive.getOdometry().getY();
  }

  public Pose2d getOdometry() {
    return drive.getOdometry();
  }

  @Override
  public void robotPeriodic() {
    // double gyroAngle = Math.toDegrees(Utils.normalizeAngle(Math.toRadians(gyro.getYaw())));
    // double cameraAngle = cameraTurret.getAngle();
    // double cameraDistance = cameraTurret.getDistance();

    // double angleDiff = Math.toRadians(cameraAngle - gyroAngle);
    // y = cameraDistance * Math.sin(angleDiff);
    // x = cameraDistance * Math.cos(angleDiff);
  }
}