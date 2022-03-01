// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.command.climb;

import java.util.List;

import frc.robot.Robot;
import frc.robot.Scheduler;
import frc.robot.command.Command;
import frc.robot.command.auto.Path.Point;
import frc.robot.control.SwerveDriveController;
import frc.robot.subsystems.Localization;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.SwingingArms;
import frc.robot.subsystems.climber.TelescopingArms;

import static frc.robot.constants.AutonomousConstants.*;

public class ResetClimberCommand implements Command {
  private Climber climber;
  private TelescopingArms tele;
  private SwingingArms swing;
  private Robot robot;
  private long startTime;
  private boolean finished = false;
  private static long executionTime = 4000;

  public ResetClimberCommand(Climber climber) {
    this.climber = climber;
    this.tele = climber.telescoping;
    this.swing = climber.swinging;
    tele.manualMove(-.1);
    swing.manualMove(-.1);
    startTime = System.currentTimeMillis();
  }
  
  @Override
  public boolean run() {
    if (System.currentTimeMillis() > startTime + executionTime) {
      tele.resetEnc();
      swing.resetEnc();
      
      tele.stop();
      swing.stop();
      return true;
    } else {
      return false;
    }
  }
}
