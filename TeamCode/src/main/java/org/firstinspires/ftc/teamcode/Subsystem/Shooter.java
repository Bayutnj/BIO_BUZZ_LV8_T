package org.firstinspires.ftc.teamcode.Subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Hardware.Motor.BangBangController;

public class Shooter extends SubsystemBase {

    private MotorGroup flyWheel;
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(1, 1);
    private BangBangController bangBangController = new BangBangController(50); // 50 ticks tolerance

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

        flyWheel.setRunMode(Motor.RunMode.RawPower);
        flyWheel.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void periodic() {
        switch (currentState) {
            case SPINS:
                setFlyWheel(1300);
                isBusy = false;
                break;

            case STOP:
            default:
                flyWheel.set(0);
                isBusy = false;
                break;
        }
    }

    private void setFlyWheel(double target) {
        double currentVelocity = flyWheel.getVelocity();
        double feedForward = feedforward.calculate(target);

        double bangControl = bangBangController.calculate(currentVelocity, target);
        double power = (bangControl > 0) ? 1.0 : feedForward;

        flyWheel.set(power);
    }

    public void setState(sState s) {
        currentState = s;
        isBusy = true;
    }

}
