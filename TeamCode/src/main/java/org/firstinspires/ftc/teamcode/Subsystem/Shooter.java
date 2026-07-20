package org.firstinspires.ftc.teamcode.Subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.InterpLUT;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Constants.ShooterConstant;
import org.firstinspires.ftc.teamcode.Controller.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Hardware.Motor.BangBangController;

public class Shooter extends SubsystemBase {

    private MotorEx fl1;
    private MotorEx fl2;
    private ServoEx hood;

    private SimpleMotorFeedforward feedforward;
    private BangBangController bangBangController;
    private VoltageSensor voltageSensor;

//    Lut = Lok up table
    private final InterpLUT velocityLUT = new InterpLUT(); // Shooter Velocity LUT
    private final InterpLUT hoodLUT = new InterpLUT(); // Hood Angle LUT
    private double distanceGoal = 0;
    private double t = 0; // Target Velocity

    public enum sState {
        SPINS,
        STOP
    }

    private  boolean isBusy = false;

    public sState currentState = sState.STOP;

    public Shooter() {
    }

    public void init(HardwareMap hardwareMap) {
        fl1 = new MotorEx(hardwareMap, RobotConstant.LEFT_FLYWHEEL, Motor.GoBILDA.BARE); // LeftFlyWheel
        fl2 = new MotorEx(hardwareMap, RobotConstant.RIGHT_FLYWHEEL, Motor.GoBILDA.BARE); // RightFlyWheel
        hood = hardwareMap.get(ServoEx.class, "hood");

        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        fl1.setRunMode(MotorEx.RunMode.RawPower);
        fl2.setRunMode(MotorEx.RunMode.RawPower);
        fl1.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.FLOAT);
        fl2.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.FLOAT);

        fl2.setInverted(true);
        hood.setRange(0.30, 0.65);
        hood.setPosition(0.30);

        feedforward = new SimpleMotorFeedforward(PIDFCoefficients.kS, PIDFCoefficients.kV);
        bangBangController = new BangBangController(ShooterConstant.TICKS_TOLERANCE); // 50 ticks tolerance

//        Shooter Data(Distance, Velocity)
        velocityLUT.add(43.6, 980);
        velocityLUT.add(75.8, 1320);
        velocityLUT.add(102.6, 1360);
        velocityLUT.add(135.6, 1740);
        velocityLUT.add(150.6, 1840);

        hoodLUT.add(43.6, 0.30);
        hoodLUT.add(75.8, 0.40);
        hoodLUT.add(102.6, 0.50);
        hoodLUT.add(135.6, 0.60);
        hoodLUT.add(150.6, 0.65);
        velocityLUT.createLUT();
        hoodLUT.createLUT();
    }

    @Override
    public void periodic() {
        double targetVel = calculateVelLUT(distanceGoal);
        double targetHood = calculateHoodLUT(distanceGoal);
        switch (currentState) {
            case SPINS:
                setFlyWheel(targetVel);
                hood.setPosition(targetHood);
                isBusy = false;
                break;

            case STOP:
            default:
                fl1.set(0);
                fl2.set(0);
                isBusy = false;
                break;
        }
    }

    public void setDistanceGoal(double d) {
        this.distanceGoal = d;
    }

    public void setDistanceGoal(Pose2D currentPosition, Pose2D targetPosition) { // INCH
        this.distanceGoal = Math.hypot(targetPosition.getX(DistanceUnit.INCH) - currentPosition.getX(DistanceUnit.INCH),
                targetPosition.getY(DistanceUnit.INCH) - currentPosition.getY(DistanceUnit.INCH));
    }

    private void setFlyWheel(double target) {
        double currentVoltage = voltageSensor.getVoltage();

        double cV1 = fl1.getVelocity();
        double cV2 = fl2.getVelocity();

        double feedForwardVolts = feedforward.calculate(target);
        double feedForwardPower = feedForwardVolts / currentVoltage;
        double out1 = bangBangController.calculate(cV1, target);
        double out2 = bangBangController.calculate(cV2, target);

        double power1 = feedForwardPower;
        double power2 = feedForwardPower;

//        double power1 = (out1 > 0) ? 1.0 : feedForwardPower;
//        double power2 = (out2 > 0) ? 1.0 : feedForwardPower;
        if (out1 > 0) {
            double error1 = target - cV1;
            power1 += (error1 / target) * ShooterConstant.BANG_GAIN;
        }
        if (out2 > 0) {
            double error2 = target - cV2;
            power2 += (error2 / target) * ShooterConstant.BANG_GAIN;
        }

        power1 = Range.clip(power1, 0, ShooterConstant.MAX_POWER);
        power2 = Range.clip(power2, 0, ShooterConstant.MAX_POWER);
        fl1.set(power1);
        fl2.set(power2);

        this.t = target;
    }

    private double calculateVelLUT(double distance) {
        double d = MathUtils.clamp(distance,
                ShooterConstant.MIN_CALIBRATED, ShooterConstant.MAX_CALIBRATED);

        return velocityLUT.get(d);
    }

    private double calculateHoodLUT(double distance) {
        double d = MathUtils.clamp(distance,
                ShooterConstant.MIN_CALIBRATED, ShooterConstant.MAX_CALIBRATED);

        return hoodLUT.get(d);
    }

    public void setState(sState s) {
        currentState = s;
        isBusy = true;
    }

    public double targetVelocity() {
        return t;
    }
    public double getVelocity() {
        return fl1.getVelocity() + fl2.getVelocity() / 2.0;
    }

}