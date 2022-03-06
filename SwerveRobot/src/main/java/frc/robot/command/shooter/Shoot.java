package frc.robot.command.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.command.CommandSequence;
import frc.robot.util.ShuffleBoard;

import static frc.robot.constants.ShooterConstants.*;

public class Shoot extends CommandSequence {
    public Shoot(TalonFX indexMotor) {
        if (ShuffleBoard.indexEnableKickback.getDouble(0) > 0.5) {
            append(new IndexBall(
                indexMotor,
                ShuffleBoard.indexKickbackSpeed.getDouble(INDEX_KICKBACK_SPEED),
                ShuffleBoard.indexKickbackTime.getNumber(INDEX_KICKBACK_TIME).intValue()
            ));
        }

        append(new IndexBall(
            indexMotor, 
            ShuffleBoard.indexSpeed.getDouble(INDEX_SPEED),
            ShuffleBoard.indexTime.getNumber(INDEX_TIME).intValue()
        ));
    }
}
