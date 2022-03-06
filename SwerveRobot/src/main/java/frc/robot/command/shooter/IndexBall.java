
package frc.robot.command.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.command.Command;
import frc.robot.util.ShuffleBoard;

import static frc.robot.constants.ShooterConstants.*;

public class IndexBall implements Command {
  private final TalonFX indexMotor;
  private final double speed;

  private int timer = 0;

  public IndexBall(TalonFX indexMotor, double speed, int time) {
    this.indexMotor = indexMotor;
    this.speed = speed;
    this.timer = time;
  }

  @Override
  public boolean run() {
    // double speed = ShuffleBoard.indexSpeed.getDouble(INDEX_SPEED);

    indexMotor.set(ControlMode.PercentOutput, speed);
    
    boolean end = --timer <= 0;

    if (end) {
      indexMotor.set(ControlMode.PercentOutput, 0);
    }

    return end;
  }
}
