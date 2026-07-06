package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.INTAKE;
import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.OUTTAKE;
import static org.firstinspires.ftc.teamcode.Subsystem.Intake.intakeState.STOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

public class MainTeleop_Mecanum extends OpMode {
    driveTrain driveTrain = new driveTrain();
    Intake intake = new Intake();

    @Override
    public void init() {
        driveTrain.init(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        driveTrain.periodic();

        driveTrain.fieldCentricMecanum(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        if (gamepad1.left_trigger > 0.01) { intake.setState(INTAKE);}
        else if (gamepad1.left_bumper) { intake.setState(OUTTAKE);}
        else { intake.setState(STOP);}
    }
}
