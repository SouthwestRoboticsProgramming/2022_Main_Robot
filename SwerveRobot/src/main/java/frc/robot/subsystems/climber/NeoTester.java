package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Robot;
import frc.robot.control.Input;
import frc.robot.subsystems.Subsystem;

import static frc.robot.constants.ClimberConstants.*;

public class NeoTester extends Subsystem {
    private final CANSparkMax left;
    private final CANSparkMax right;

    public NeoTester() {
        left = new CANSparkMax(CLIMBER_LEFT_SWING_MOTOR_ID, MotorType.kBrushless);
        left.setIdleMode(IdleMode.kCoast);

        right = new CANSparkMax(CLIMBER_RIGHT_SWING_MOTOR_ID, MotorType.kBrushless);
        right.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void teleopPeriodic() {
        Input input = Robot.INSTANCE.input;

        left.set(input.getNeoTestLeft());
        right.set(input.getNeoTestRight());
    }
}
