package org.firstinspires.ftc.teamcode.Subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;

public class Shooter extends SubsystemBase {

    private MotorGroup flyWheel;

    public enum sState {
        SPINS,
        STOP
    }

    private boolean isBusy = false;

    public sState currentState = sState.STOP;

    public Shooter() {
    }

    public void init(HardwareMap HwMap) {
        flyWheel = new MotorGroup(
                new Motor(HwMap, RobotConstant.LEFT_FLYWHEEL, 28, 6000),
                new Motor(HwMap, RobotConstant.RIGHT_FLYWHEEL, 28, 6000));

        flyWheel.setRunMode(Motor.RunMode.VelocityControl);
        flyWheel.setVeloCoefficients(0.05, 0,0);
        flyWheel.setFeedforwardCoefficients(0, 0.7);
    }

    @Override
    public void periodic() {
        switch (currentState) {
            case SPINS:
                flyWheel.set(0.6);
                isBusy = false;
                break;

            case STOP:
            default:
                flyWheel.set(0);
                isBusy = false;
                break;
        }
    }

    public void setState(sState s) {
        currentState = s;
        isBusy = true;
    }

}
