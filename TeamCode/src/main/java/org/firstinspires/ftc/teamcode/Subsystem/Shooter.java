package org.firstinspires.ftc.teamcode.Subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Constants.ShooterConstant;
import org.firstinspires.ftc.teamcode.Controller.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Hardware.Motor.BangBangController;

public class Shooter extends SubsystemBase {

    private MotorGroup flyWheel;
    private SimpleMotorFeedforward feedforward;
    private BangBangController bangBangController;
    private VoltageSensor voltageSensor;

//    Lut = Lok up table
    private final InterpLUT lut = new InterpLUT();

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

        voltageSensor = HwMap.get(VoltageSensor.class, "Control Hub");

        flyWheel.setRunMode(Motor.RunMode.RawPower);
        flyWheel.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);

        feedforward = new SimpleMotorFeedforward(PIDFCoefficients.kS, PIDFCoefficients.kV);
        bangBangController = new BangBangController(ShooterConstant.TICKS_TOLERANCE); // 50 ticks tolerance

//        Shooter Data(Distance, Velocity)
        lut.add(43.6, 980);
        lut.add(75.8, 1320);
        lut.add(102.6, 1360);
        lut.add(135.6, 1740);
        lut.add(150.6, 1840);
        lut.createLUT();
    }

    @Override
    public void periodic() {
        double target = calculatePower(0);
        switch (currentState) {
            case SPINS:
                setFlyWheel(target);
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
        double currentVoltage = voltageSensor.getVoltage();

        double currentVelocity = flyWheel.getVelocity();
        double feedForwardVolts = feedforward.calculate(target);

        double feedForwardPower = feedForwardVolts / currentVoltage;
        double bangControl = bangBangController.calculate(currentVelocity, target);
        double power = (bangControl > 0) ? 1.0 : feedForwardPower;

        power = Range.clip(power, 0, ShooterConstant.MAX_POWER);
        flyWheel.set(power);
    }

    private double calculatePower(double distance) {
        return lut.get(distance);
    }

    public void setState(sState s) {
        currentState = s;
        isBusy = true;
    }

}