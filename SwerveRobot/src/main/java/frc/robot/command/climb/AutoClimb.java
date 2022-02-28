// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.command.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.climber.NewSwingingArm;
import frc.robot.subsystems.climber.NewTelescopingArm;
import frc.robot.subsystems.climber.SwingingArms;
import frc.robot.subsystems.climber.TelescopingArms;
import frc.robot.util.ShuffleBoard;

public class AutoClimb extends CommandBase {
  private int climbStep = 0;
  private Robot robot;
    //0 = arms down, ready to lift
    // PAUSE
    //1 = arms extended, ready to clamp onto 2
    //2 = arms retract, ready for rotator
    //3 = rotator arms rotate robot to face 3
    //4 = extend arms to clamp onto 3
    //5 = rotator applies pressure on extender arms
    // PAUSE
    //GoTo step 2

  private NewTelescopingArm teleLeft, teleRight;
  private NewSwingingArm swingLeft, swingRight;
  
  /** Creates a new ClimbToTop. */
  public AutoClimb( NewTelescopingArm teleLeft, NewTelescopingArm teleRight, 
                    NewSwingingArm swingLeft, NewSwingingArm swingRight, Robot robot) {
    this.teleLeft = teleLeft;
    this.teleRight = teleRight;
    this.swingLeft = swingLeft;
    this.swingRight = swingRight;
    this.robot = robot;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    teleLeft.extendToDistance(ShuffleBoard.climbTune1TeleHeight.getDouble(0));
    teleRight.extendToDistance(ShuffleBoard.climbTune1TeleHeight.getDouble(0));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (climbStep) {
      case 1:
        //Whe clicked, arms set to retract
        if (robot.input.getNextclimbStep()) {
          teleLeft.extendToDistance(ShuffleBoard.climbTune2TeleHeight.getDouble(0));
          teleRight.extendToDistance(ShuffleBoard.climbTune2TeleHeight.getDouble(0));
          climbStep = 2;
        }
        break;
      case 2:
        //wait for arms to retract, then rotate robot
        if (  checkIfValsInTollerence(teleLeft.getPos(), teleRight.getPos(), 
              ShuffleBoard.climbTune2TeleHeight.getDouble(0), ShuffleBoard.climbTuneTeleTolerence.getDouble(0))) {
          swingLeft.swingToAngle(ShuffleBoard.climbTune3SwingAngle.getDouble(0));
          swingRight.swingToAngle(ShuffleBoard.climbTune3SwingAngle.getDouble(0));
          climbStep = 3;
        }
        break;
      case 3:
        if (  checkIfValsInTollerence(swingLeft.getPos(), swingRight.getPos(), 
              ShuffleBoard.climbTune3SwingAngle.getDouble(0), ShuffleBoard.climbTuneSwingTolerence.getDouble(0))) {  
          teleLeft.extendToDistance(ShuffleBoard.climbTune4TeleHeight.getDouble(0));
          teleRight.extendToDistance(ShuffleBoard.climbTune4TeleHeight.getDouble(0));
          climbStep = 4;
        }
        break;
      case 4:
        if (  checkIfValsInTollerence(teleLeft.getPos(), teleRight.getPos(), 
              ShuffleBoard.climbTune4TeleHeight.getDouble(0), ShuffleBoard.climbTuneTeleTolerence.getDouble(0))) {  
          swingLeft.swingToAngle(ShuffleBoard.climbTune5SwingAngle.getDouble(0));
          swingRight.swingToAngle(ShuffleBoard.climbTune5SwingAngle.getDouble(0));
          climbStep = 5;
        }
        break;
      case 5:
        if (  checkIfValsInTollerence(swingLeft.getPos(), teleRight.getPos(), 
              ShuffleBoard.climbTune5SwingAngle.getDouble(0), ShuffleBoard.climbTuneSwingTolerence.getDouble(0))) {  
            teleLeft.extendToDistance(ShuffleBoard.climbTune2TeleHeight.getDouble(0));
            teleRight.extendToDistance(ShuffleBoard.climbTune2TeleHeight.getDouble(0));
          climbStep = 2;
        }
        break;
      default:
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private boolean checkIfValsInTollerence(double encoder1, double encoder2, double setPoint, double tollerence) {
        boolean enc1Tollerence = Math.abs(encoder1-setPoint) < tollerence;
        boolean enc2Tollerence = Math.abs(encoder2-setPoint) < tollerence;
      if (enc1Tollerence && enc2Tollerence) {
        return true;
      } else {
        return false;
      }
  }
}