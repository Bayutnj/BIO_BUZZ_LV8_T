package org.firstinspires.ftc.teamcode.Subsystem.Drive;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.drivebase.DifferentialDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Localization.Enum.DriveTrainType;
import org.firstinspires.ftc.teamcode.Localization.Localizer;

public class TankDrive extends SubsystemBase implements DriveTrain {
    private Motor left;
    private Motor right;

    private final DifferentialDrive drive;
    private final Localizer localizer;

    public TankDrive(HardwareMap hardwareMap, Localizer localizer) {
        this.localizer = localizer;

        left = new Motor(hardwareMap, RobotConstant.LEFT_MOTOR);
        right = new Motor(hardwareMap, RobotConstant.RIGHT_MOTOR);

        left.setRunMode(Motor.RunMode.RawPower);
        right.setRunMode(Motor.RunMode.RawPower);

        drive = new DifferentialDrive(true, left, right);
    }

    @Override
    public void ArcadeDrive(double forward, double turn) {
        drive.arcadeDrive(forward, turn);
    }

    @Override
    public void tankDrive(double leftPower, double rightPower) {
        drive.tankDrive(leftPower, rightPower);
    }

    @Override
    public void driveRobotCentric(double forward, double lateral, double turn) {}
    @Override
    public void driveFieldCentric(double forward, double lateral, double turn) {}

    @Override
    public void stop() {
        left.stopMotor();
        right.stopMotor();
    }

    @Override
    public void resetMotorEncoder() {
        left.encoder.reset();
        right.encoder.reset();
    }

    @Override
    public void setBrakeMode(boolean brakeMode) {
        Motor.ZeroPowerBehavior behavior = brakeMode ? Motor.ZeroPowerBehavior.BRAKE : Motor.ZeroPowerBehavior.FLOAT;

        left.setZeroPowerBehavior(behavior);
        right.setZeroPowerBehavior(behavior);
    }

}
