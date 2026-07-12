package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.INTAKE;
import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.OUTTAKE;
import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.STOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

public class MainTeleop_TANK extends OpMode {
    driveTrain driveTrain;
    Intake intake;

    @Override
    public void init() {
        driveTrain.init(hardwareMap, RobotConstant.STARTING_POSE);
        intake.init(hardwareMap);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        driveTrain.periodic();

        driveTrain.ArcadeDrive(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (gamepad1.left_trigger > 0.01) { intake.setState(INTAKE);}
        else if (gamepad1.left_bumper) { intake.setState(OUTTAKE);}
        else { intake.setState(STOP);}

        telemetry.addData("X Pose", driveTrain.getPoseX());
        telemetry.addData("Y Pose", driveTrain.getPoseY());
        telemetry.addData("Heading", driveTrain.getPoseHeading());
        telemetry.addData("Intake State", intake.getCurrentState());
        telemetry.update();
    }
}
