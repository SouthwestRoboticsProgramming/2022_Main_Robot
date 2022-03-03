package frc.robot.subsystems.climber;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.util.ShuffleBoard;
import frc.robot.util.Utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import static frc.robot.constants.ClimberConstants.*;

public class NewSwingingArm {
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;
    
    private PIDController pid;

    private boolean resetting;

    public NewSwingingArm(int motorID) {
        motor = new CANSparkMax(motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setInverted(true);

        encoder = motor.getEncoder();
        encoder.setPosition(0);
        
        pid = new PIDController(0,0,0);
        // pid.setTolerance(CLIMBER_SWING_TOLERANCE);

        resetting = false;
    }


    public double getCurrentAngle() {
        System.out.println("Encoder: " + encoder.getPosition());
        
        // if (count++ > 100)
        // throw new RuntimeException();
    
        double currentPose = encoder.getPosition() / CLIMBER_SWING_ROTS_PER_INCH + CLIMBER_STARTING_DIST;
        System.out.println("Pose: " + currentPose + " is good? " + (currentPose < CLIMBER_SWING_BASE + CLIMBER_SWING_ARM));

        double currentAngle = Math.acos(
            (
                CLIMBER_SWING_BASE * CLIMBER_SWING_BASE 
                + CLIMBER_SWING_ARM * CLIMBER_SWING_ARM 
                - currentPose * currentPose
            ) / (2 * CLIMBER_SWING_ARM * CLIMBER_SWING_BASE)
        );
        return Math.toDegrees(currentAngle);
    }

    private void updatePid(boolean loaded) {
        if (loaded) {
            pid.setPID(
                ShuffleBoard.climberSwingLoadedKP.getDouble(CLIMBER_SWING_MOTOR_KP),
                ShuffleBoard.climberSwingLoadedKI.getDouble(CLIMBER_SWING_MOTOR_KI),
                ShuffleBoard.climberSwingLoadedKD.getDouble(CLIMBER_SWING_MOTOR_KD)
            );
        } else {
            pid.setPID(
                ShuffleBoard.climberSwingKP.getDouble(CLIMBER_SWING_MOTOR_KP),
                ShuffleBoard.climberSwingKI.getDouble(CLIMBER_SWING_MOTOR_KI),
                ShuffleBoard.climberSwingKD.getDouble(CLIMBER_SWING_MOTOR_KD)
            );
        }
    }

    

    public void swingToAngle(double degrees, boolean loaded) {
        // System.out.println("Swinging to angle: " + degrees);

        // if (resetting) {return;}
        updatePid(loaded);

        double currentAngle = getCurrentAngle();
    
        double out = pid.calculate(currentAngle, degrees);
        out = Utils.clamp(out, -0.5, 0.5);
        System.out.println("Current Angle: " + currentAngle + " | SetAngle: " + degrees + " | Output:" + out);
        // System.out.println("PID Output: " + out);
        motor.set(out);
    }

    public void resetEnc() {
        encoder.setPosition(0);
    }

    public void stop() {
        motor.stopMotor();
    }

    public void manualMove(double speed) {
        motor.set(speed);
    }

    public double getPos() {
        // return encoder.getPosition() + CLIMBER_STARTING_DIST;
        return getCurrentAngle();
        // throw new AssertionError("Don't use");
    }

    public void zero() {
        encoder.setPosition(0);
    }

    public void setResetting(boolean resetting) {
        this.resetting = resetting;
    }
}
