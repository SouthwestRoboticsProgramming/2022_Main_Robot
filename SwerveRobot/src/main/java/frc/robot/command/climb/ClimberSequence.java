// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.command.climb;

import frc.robot.Robot;
import frc.robot.command.CommandSequence;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.NewSwingingArm;
import frc.robot.subsystems.climber.NewTelescopingArm;
import frc.robot.subsystems.climber.SwingingArms;
import frc.robot.subsystems.climber.TelescopingArms;

public final class ClimberSequence extends CommandSequence {
  public ClimberSequence(Robot robot, Climber climber) {

    append(new ResetClimberCommand(climber));
    append(new AutoClimbCommand(robot, climber));
  }
}
