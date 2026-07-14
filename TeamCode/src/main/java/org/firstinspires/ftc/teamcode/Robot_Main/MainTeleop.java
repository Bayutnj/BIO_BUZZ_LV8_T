package org.firstinspires.ftc.teamcode.Robot_Main;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.Alliance;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Robot;

@TeleOp
public class MainTeleop extends CommandOpMode {
    private Robot robot;
    private GamepadEx gamepadEx;
    @Override
    public void initialize() {
        robot = new Robot(
                hardwareMap, Alliance.updatePose
        );
        gamepadEx = new GamepadEx(gamepad1);

        register(robot.getLocalizer());
        register(robot.getDriveTrain());
    }

    @Override
    public void run() {
        super.run();
        robot.periodic();

        robot.getDriveTrain().driveFieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        new Trigger(() -> gamepadEx.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.01)
                .whenActive(() -> robot.setIntake(Intake.intakeState.INTAKE))
                .whenInactive(new InstantCommand(() -> robot.setIntake(Intake.intakeState.STOP)));


        telemetry.addData("X", robot.getLocalizer().getX());
        telemetry.addData("Y", robot.getLocalizer().getY());
        telemetry.addData("Heading", robot.getLocalizer().getHeading());
    }
}
