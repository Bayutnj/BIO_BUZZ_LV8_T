package org.firstinspires.ftc.teamcode.Robot_Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.Alliance;
import org.firstinspires.ftc.teamcode.Constants.Pose.targetPose;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

@Autonomous
public class Autotest extends LinearOpMode {
    driveTrain driveTrain = new driveTrain();
    Intake intake = new Intake();
    Pose2D currentPose;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain.init(hardwareMap, RobotConstant.autoStartPose);
        intake.init(hardwareMap);

        waitForStart();

        driveTrain.driveTo(targetPose.firstPath, currentPose, 1.0);
        intake.setState(Intake.intakeState.INTAKE);
        driveTrain.driveTo(targetPose.secondPath, currentPose, 1.0);
        intake.setState(Intake.intakeState.STOP);
        driveTrain.turnTo(Math.toRadians(90), 1.0, this);

        while (opModeIsActive()) {
            driveTrain.periodic();

            Pose2D currentPosition = driveTrain.getPose();
            this.currentPose = currentPosition;

            Alliance.updatePose = driveTrain.getPose();
        }
    }
}
