package org.firstinspires.ftc.teamcode.Subsystem.ARM;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class PivotArm_GABUT_DULU_YAAA extends SubsystemBase {
    private Motor armMotor;
    private SimpleServo servo;
    private ArmFeedforward feedforward;
    private PIDFController pidfController;

//    private double targetPos = 0.0;
    private double maxAngle = Math.toRadians(90);
    private double minAngle = Math.toRadians(20);

    public enum ArmState {
        LIFTING,
        PULL_OUT,
        LIFT_DOWN,
        STOP
    }
    public ArmState currentState = ArmState.STOP;

    public PivotArm_GABUT_DULU_YAAA() {}

    public void init(HardwareMap HwMap) {
        armMotor = new Motor(HwMap, "ARM", 28, 435);
        feedforward = new ArmFeedforward(0.00015, 0.01 ,0.005);
        pidfController = new PIDFController(0.001, 0,0.00001 ,0.005);

        servo = new SimpleServo(HwMap, "PULLOUT", 45, 120, AngleUnit.DEGREES);
        servo.setInverted(true);
        servo.turnToAngle(45);
    }

//    public void setTargetPosition(double posDegree) {
//        this.targetPos = Range.clip(Math.toRadians(targetPos), minAngle, maxAngle);
//    }

    private void doArm(double posDegree) {
        double targetPos = Range.clip(Math.toRadians(posDegree), minAngle, maxAngle);

        double posRad = getCurrentRadians();

//                Additional power (feedForward)
        double ff = feedforward.calculate(posRad, 0);
//                Pushing to the targetPosition, targetposition in Radian unit
        double pid = pidfController.calculate(posRad, targetPos);
        armMotor.set(ff + pid);
    }

    @Override
    public void periodic() {
        switch (currentState) {
            case LIFTING:

                doArm(90);
                break;

            case PULL_OUT:
                servo.turnToAngle(120);
                break;

            case LIFT_DOWN:
                doArm(20);
                break;

        }
    }

    public double getCurrentRadians() {
        return armMotor.getCurrentPosition() * (2 * Math.PI / (28 * (((1+(46/17))) * (1+(46/17)))));
    }

    public void setState(ArmState s) {
        currentState = s;
    }
}
