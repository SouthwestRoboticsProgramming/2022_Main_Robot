// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.command.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.NewSwingingArm;
import frc.robot.subsystems.climber.NewTelescopingArm;

// nate code; dont use it
@Deprecated
public class ResetClimber extends CommandBase {
  private NewTelescopingArm teleLeft, teleRight;
  private NewSwingingArm swingLeft, swingRight;
  /** Creates a new ResetClimber. */
  public ResetClimber(  NewTelescopingArm teleLeft, NewTelescopingArm teleRight, 
                        NewSwingingArm swingLeft, NewSwingingArm swingRight) {
    this.teleLeft = teleLeft;
    this.teleRight = teleRight;
    this.swingLeft = swingLeft;
    this.swingRight = swingRight;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    teleLeft.manualMove(.1);
    teleRight.manualMove(.1);
    swingLeft.manualMove(.2);
    swingRight.manualMove(.2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    teleLeft.resetEnc();
    teleRight.resetEnc();
    swingLeft.resetEnc();
    swingRight.resetEnc();
    
    teleLeft.manualMove(0);
    teleRight.manualMove(0);
    swingLeft.manualMove(0);
    swingRight.manualMove(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
