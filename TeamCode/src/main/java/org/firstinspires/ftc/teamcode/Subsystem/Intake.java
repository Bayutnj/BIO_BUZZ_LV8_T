package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Motor.motorInitialize;

public class Intake {
    motorInitialize intake;
    HardwareMap hwMap;
    private boolean isBusy = false;

    public enum intakeState {
        INTAKE,
        OUTTAKE,
        STOP
    }

    public intakeState currentState = Intake.intakeState.STOP;

    public void init(HardwareMap hwMap) {
        intake = new motorInitialize(hwMap,
                RobotConstant.INTAKE,
                RobotConstant.INTAKE_DIRECTION,
                RobotConstant.INTAKE_BEHAVIOR,
                RobotConstant.INTAKE_MODE,
                RobotConstant.INTAKE_MOTOR,
                RobotConstant.INTAKE_LIMIT
        );
    }

    public void update() {
        switch (currentState) {
            case INTAKE:
                intake.setPower(1.0);

                isBusy = false;
                break;

            case OUTTAKE:
                intake.setPower(-1);

                isBusy = false;
                break;

            case STOP:
                intake.setPower(0);

                isBusy = false;
                break;
        }
    }

    public void setState(intakeState s) {
        currentState = s;
        isBusy = true;
    }

    public intakeState getCurrentState() {
        return currentState;
    }
}
