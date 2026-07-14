package org.firstinspires.ftc.teamcode.Subsystem.Drive;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Localization.Localizer;

public class MecanumDrive extends SubsystemBase implements DriveTrain {
    private final Motor leftFront;
    private final Motor rightFront;
    private final Motor leftBack;
    private final Motor rightBack;

    private final com.arcrobotics.ftclib.drivebase.MecanumDrive drive;
    private final Localizer localizer;

    public MecanumDrive(HardwareMap hardwareMap, Localizer localizer) {
        this.localizer = localizer;

        leftFront = new Motor(hardwareMap, RobotConstant.FRONT_LEFT_MOTOR);
        rightFront = new Motor(hardwareMap, RobotConstant.FRONT_RIGHT_MOTOR);
        leftBack = new Motor(hardwareMap, RobotConstant.LEFT_MOTOR);
        rightBack = new Motor(hardwareMap, RobotConstant.RIGHT_MOTOR);

//        leftFront.setInverted(true);
//        leftBack.setInverted(false);
//
//        leftBack.setInverted(true);
//        rightBack.setInverted(false);

        leftBack.setRunMode(Motor.RunMode.RawPower);
        rightBack.setRunMode(Motor.RunMode.RawPower);
        leftFront.setRunMode(Motor.RunMode.RawPower);
        rightFront.setRunMode(Motor.RunMode.RawPower);

        drive = new com.arcrobotics.ftclib.drivebase.MecanumDrive(true, leftFront, rightFront,
                leftBack, rightBack);
    }

    @Override
    public void driveRobotCentric(double forward, double lateral, double turn) {
        drive.driveRobotCentric(
                lateral, forward, turn
        );
    }

    @Override
    public void driveFieldCentric(double forward, double lateral, double turn) {
        drive.driveFieldCentric(
                lateral, forward, turn, localizer.getHeading()
        );
    }

    @Override
    public void ArcadeDrive(double forward, double turn) {}
    @Override
    public void tankDrive(double leftPower, double rightPower) {}

    @Override
    public void stop() {
        leftFront.stopMotor();
        rightFront.stopMotor();
        leftBack.stopMotor();
        rightBack.stopMotor();
    }

    @Override
    public void resetMotorEncoder() {
        leftFront.encoder.reset();
        rightFront.encoder.reset();
        leftBack.encoder.reset();
        rightBack.encoder.reset();
    }

    @Override
    public void setBrakeMode(boolean brakeMode) {
        Motor.ZeroPowerBehavior behavior = brakeMode ? Motor.ZeroPowerBehavior.BRAKE : Motor.ZeroPowerBehavior.FLOAT;

        leftFront.setZeroPowerBehavior(behavior);
        rightFront.setZeroPowerBehavior(behavior);
        leftBack.setZeroPowerBehavior(behavior);
        rightBack.setZeroPowerBehavior(behavior);
    }

}
