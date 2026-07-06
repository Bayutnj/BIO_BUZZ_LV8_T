package org.firstinspires.ftc.teamcode.FTCLib;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

@TeleOp(group = "FTCLib")
public class FTCLib_TELEOP extends CommandOpMode {
    private driveTrain driveTrain = new driveTrain();
    private Intake intake = new Intake();
    private GamepadEx gamepadEx1;

    @Override
    public void initialize() {
        super.reset();

        driveTrain.init(hardwareMap);
        intake.init(hardwareMap);
        gamepadEx1 = new GamepadEx(gamepad1);
        register(driveTrain, intake);

        new Trigger(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .whenActive(() -> intake.setState(Intake.intakeState.INTAKE))
                .whenInactive(new InstantCommand(() -> intake.setState(Intake.intakeState.STOP)));

        gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> intake.setState(Intake.intakeState.OUTTAKE)))
                .whenInactive(new InstantCommand(() -> intake.setState(Intake.intakeState.STOP)));


    }

    @Override
    public void run() {
        super.run();
//        driveTrain.periodic();
//        intake.periodic();
        driveTrain.ArcadeDrive(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("Current Intake State", intake.getCurrentState());
        telemetry.update();
    }
}
