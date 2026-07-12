package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

public class Autotest extends LinearOpMode {
    driveTrain driveTrain = new driveTrain();
    Intake intake = new Intake();

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain.init(hardwareMap, RobotConstant.STARTING_POSE);
        intake.init(hardwareMap);

        waitForStart();

        driveTrain.driveTo(false,12, Math.toRadians(90), 1, 1.0, this);
        intake.setState(Intake.intakeState.INTAKE);
        driveTrain.driveTo(true,-12, Math.toRadians(-90), 1, 1.0, this);
        intake.setState(Intake.intakeState.STOP);
        driveTrain.turnTo(Math.toRadians(90), 1.0, this);

        while (opModeIsActive()) {
            driveTrain.periodic();
        }
    }
}
