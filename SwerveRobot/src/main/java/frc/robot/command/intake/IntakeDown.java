package frc.robot.command.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.command.Command;

import static frc.robot.constants.IntakeConstants.*;

public class IntakeDown implements Command {
  private final TalonSRX motor;

  private int timer = 0;

  public IntakeDown(TalonSRX motor) {
    this.motor = motor;
  }

  @Override
  public boolean run() {
    motor.set(ControlMode.PercentOutput, -INTAKE_RETRACTION_SPEED);
    
    return ++timer >= INTAKE_TIME;
  }
}
