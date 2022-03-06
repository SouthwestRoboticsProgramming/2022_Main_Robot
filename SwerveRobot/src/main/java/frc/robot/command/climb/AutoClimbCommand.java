// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.command.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.NewSwingingArm;
import frc.robot.subsystems.climber.NewTelescopingArm;
import frc.robot.util.ShuffleBoard;
import frc.robot.util.Utils;

import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotState;
import frc.robot.command.Command;
import frc.robot.command.auto.Path.Point;
import frc.robot.control.Input;
import frc.robot.control.SwerveDriveController;
import frc.robot.subsystems.Localization;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.SwingingArms;
import frc.robot.subsystems.climber.TelescopingArms;

import static frc.robot.constants.AutonomousConstants.*;

public class AutoClimbCommand implements Command {
  private int climbStep = 0;
  private double teleSetpoint=0, swingSetpoint=90;
  private boolean teleLoaded=false, swingLoaded=false;
  private Robot robot;
  private long lastStepTime = 0;
  private static boolean checkpoints = true;
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
  public AutoClimbCommand(Robot robot, Climber climber) {
    this.teleLeft = climber.telescoping.left;
    this.teleRight = climber.telescoping.right;
    this.swingLeft = climber.swinging.left;
    this.swingRight = climber.swinging.right;
    this.robot = robot;
  }

  // Called when the command is initially scheduled.
  
  public void initialize() {
    
  }

  private void switchToStep(int step) {
    climbStep = step;
    System.out.println("Switching to step: " + step);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public boolean run() {
    

    Input input = Robot.INSTANCE.input;
    teleSetpoint += input.getTeleManual()/100;
    swingSetpoint += input.getSwingManual()/2;

    boolean teleTol = checkIfValsInTollerence(teleLeft.getPos(), teleLeft.getPos(), 
        teleSetpoint, ShuffleBoard.climbTuneTeleTolerence.getDouble(0));
    double teleOffset = teleSetpoint - teleLeft.getPos();
    ShuffleBoard.valueA.setString(String.valueOf(teleTol));
    // ShuffleBoard.valueB.setString(String.valueOf(teleSetpoint));
    // ShuffleBoard.valueC.setString(String.valueOf(teleLeft.getPos()));
    ShuffleBoard.valueB.setString(String.valueOf(teleOffset));

    boolean swingTol = checkIfValsInTollerence(swingLeft.getPos(), swingLeft.getPos(), 
        swingSetpoint, ShuffleBoard.climbTuneSwingTolerence.getDouble(0));
    double swingOffset = swingSetpoint - swingLeft.getPos();
    ShuffleBoard.valueC.setString(String.valueOf(swingTol));
    // ShuffleBoard.valueB.setString(String.valueOf(swingSetpoint));
    // ShuffleBoard.valueC.setString(String.valueOf(swingLeft.getPos()));
    ShuffleBoard.valueD.setString(String.valueOf(swingOffset));
    ShuffleBoard.valueE.setString(String.valueOf(climbStep));

    // System.out.println("AutoClimbCommand.run() - (" + input.getNextStep() + ", " + input.getPreviousStep() + ")");
    // if (System.currentTimeMillis() > lastStepTime + (long)1000) {
    //   if (input.getNextStep()) {
    //     climbStep++;
    //     lastStepTime = System.currentTimeMillis();
    //     System.out.println("AutoClimbCommand.run() - Case " + climbStep);
    //   }
    //   if (input.getPreviousStep()) {
    //     climbStep--;
    //     lastStepTime = System.currentTimeMillis();
    //     System.out.println("AutoClimbCommand.run() - Case " + climbStep);
    //   }
    // }

    switch (climbStep) {
      case 0:
        if (robot.input.getNextStep()) {
          switchToStep(1);
        }
        break;
      case 1:
        teleSetpoint = ShuffleBoard.climbTune1TeleHeight.getDouble(0);
        swingSetpoint = ShuffleBoard.climbTune1SwingAngle.getDouble(0);
        teleLoaded = false;
        swingLoaded = false;
        // Whe clicked, arms set to retract
        if (teleInTol() && swingInTol() && robot.input.getNextStep()) {switchToStep(2);}
        break;
      case 2:
        teleSetpoint = ShuffleBoard.climbTune2TeleHeight.getDouble(0);
        teleLoaded = true;
        //wait for arms to retract, then rotate robot
        if (teleInTol()) {switchToStep(3);}
        break;
      case 3:
        swingSetpoint = ShuffleBoard.climbTune3SwingHandoffAngle.getDouble(0);
        swingLoaded = false;
        if (swingInTol()) {switchToStep(4);}
        break;
      case 4:
        teleSetpoint = ShuffleBoard.climbTune4TeleHeight.getDouble(0);
        swingSetpoint = ShuffleBoard.climbTune5SwingFinishAngle.getDouble(0);
        teleLoaded = false;
        swingLoaded = true;
        if (teleInTol()&&swingInTol()) {switchToStep(5);}
        break;
      case 5:
        teleSetpoint = ShuffleBoard.climbTune6TeleHeight.getDouble(0);
        if (teleInTol()) {switchToStep(6);}
        break;
      case 6:
        swingSetpoint = ShuffleBoard.climbTune7SwingAngle.getDouble(0);
        teleLoaded = false;
        if (swingInTol()) {switchToStep(7);}
        break;
      case 7:
        teleSetpoint = ShuffleBoard.climbTune4TeleHeight.getDouble(0);
        swingSetpoint = ShuffleBoard.climbTune7SwingAngle.getDouble(0);
        teleLoaded = false;
        if (Robot.INSTANCE.input.getUndoStep()) {switchToStep(9); }
        if (teleInTol() && swingInTol()) {switchToStep(8);}
        break;
      case 8:
        swingSetpoint = ShuffleBoard.climbTune1SwingAngle.getDouble(0);
        teleLoaded = false;
        if (swingInTol() && robot.input.getNextStep()) {switchToStep(2);}
        break;
      case 9: // When resetting
        teleSetpoint = ShuffleBoard.climbTune6TeleHeight.getDouble(0);
        swingSetpoint = ShuffleBoard.climbTune5SwingFinishAngle.getDouble(0);
        if (teleInTol() && swingInTol()) {switchToStep(6);}
        break;
      default:
        break;
    }
    teleSetpoint = Utils.clamp(teleSetpoint, 0, 1);
    swingSetpoint = Utils.clamp(swingSetpoint, 55, 120);
    // System.out.println("AutoClimbCommand.run() - " + swingSetpoint);
    teleLeft.extendToDistance(teleSetpoint, teleLoaded);
    teleRight.extendToDistance(teleSetpoint, teleLoaded);
    swingLeft.swingToAngle(swingSetpoint, swingLoaded);
    swingRight.swingToAngle(swingSetpoint, swingLoaded);
    if (robot.state == RobotState.DISABLED) {
      return true;
    } else {
      return false;
    }
  }

  // Called once the command ends or is interrupted.
  public void end() {}

  private boolean teleInTol() {
    boolean valInTol = checkIfValsInTollerence(teleLeft.getPos(), teleRight.getPos(), 
    teleSetpoint, ShuffleBoard.climbTuneTeleTolerence.getDouble(0));
    if (checkpoints) {
      return valInTol && robot.input.getNextStep();
    } else {
      return valInTol;
    }
  }

  private boolean swingInTol() {
    boolean valInTol = checkIfValsInTollerence(swingLeft.getPos(), swingLeft.getPos(), 
    swingSetpoint, ShuffleBoard.climbTuneSwingTolerence.getDouble(0));
    if (checkpoints) {
      return valInTol && robot.input.getNextStep();
    } else {
      return valInTol;
    }
  }

  private boolean checkIfValsInTollerence(double encoder1, double encoder2, double setPoint, double tollerence) {
        boolean enc1Tollerence = Math.abs(encoder1-setPoint) < tollerence;
        boolean enc2Tollerence = Math.abs(encoder2-setPoint) < tollerence;
      return (enc1Tollerence && enc2Tollerence);
  }
}
