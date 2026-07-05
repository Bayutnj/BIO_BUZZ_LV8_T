package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.drivebase.DifferentialDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;    
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

@TeleOp(group = "FTCLib")
public class FTCLib_TELEOP extends CommandOpMode {
    DifferentialDrive drive;
    driveTrain driveTrain = new driveTrain();
    Intake intake = new Intake();
    GamepadEx gamepadEx1;

    @Override
    public void initialize() {
        driveTrain.init(hardwareMap);
        intake.init(hardwareMap);
        gamepadEx1 = new GamepadEx(gamepad1);

        MotorEx leftMotor = new MotorEx(hardwareMap, RobotConstant.LEFT_MOTOR);
        MotorEx rightMotor = new MotorEx(hardwareMap, RobotConstant.RIGHT_MOTOR);
        drive = new DifferentialDrive(leftMotor, rightMotor);

        new GamepadButton(gamepadEx1, GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(() -> driveTrain.turnTo(Math.toRadians(90), 1, this)));

        new Trigger(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .whileActiveContinuous(new InstantCommand(() -> intake.setState(Intake.intakeState.INTAKE)))
                .whenInactive(new InstantCommand(() -> intake.setState(Intake.intakeState.STOP)));

        new GamepadButton(gamepadEx1, GamepadKeys.Button.LEFT_BUMPER)
                .whileActiveContinuous(new InstantCommand(() -> intake.setState(Intake.intakeState.OUTTAKE)))
                .whenInactive(new InstantCommand(() -> intake.setState(Intake.intakeState.STOP)));

    }

    @Override
    public void run() {
        super.run();
        driveTrain.update();
        intake.update();

        drive.arcadeDrive(-gamepad1.left_stick_y, gamepad1.right_stick_x, true);
    }
}
