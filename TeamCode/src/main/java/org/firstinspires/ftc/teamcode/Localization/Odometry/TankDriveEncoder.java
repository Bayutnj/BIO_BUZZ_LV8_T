package org.firstinspires.ftc.teamcode.Localization.Odometry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.FTC_Dashboard;
import org.firstinspires.ftc.teamcode.Subsystem.Robot;

public class TankDriveEncoder extends SubsystemBase implements Encoder {
    private final  Motor.Encoder leftEnc, rightEnc;
    private final Motor lm, rm;
    private double lastLeftEnc, lastRightEnc;
    private IMU imu;
    private Pose2D pose;

    private  double x;
    private double y;
    private double heading;
    private final double headingOffset;
    private final double ticksPerInch;

    public TankDriveEncoder(Pose2D startPose, IMU imu, Motor lm, Motor rm) {
        this.lm = lm;
        this.rm = rm;

        leftEnc = lm.encoder;
        rightEnc = rm.encoder;

        this.imu = imu;
        imu.resetYaw();

        ticksPerInch = (RobotConstant.DRIVE_TRAIN_MOTOR.getTicksPerRevolution()) / (RobotConstant.wheelDiameter * Math.PI);
        pose = startPose;
        x = pose.getX(DistanceUnit.INCH);
        y = pose.getY(DistanceUnit.INCH);
        headingOffset = pose.getHeading(AngleUnit.RADIANS) - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    @Override
    public void update() {
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + headingOffset;

        int leftTicks = leftEnc.getPosition();
        int rightTicks =  rightEnc.getPosition();

        double currentLeftInch = (leftTicks - lastLeftEnc) * ticksPerInch;
        double currentRightInch = (rightTicks - lastRightEnc) * ticksPerInch;
        lastRightEnc = rightTicks;
        lastLeftEnc = leftTicks;

        double robotForward = (currentLeftInch + currentRightInch) / 2.0;

        x += robotForward * Math.cos(heading);
        y += robotForward * Math.sin(heading);

        pose = new Pose2D(
                DistanceUnit.INCH, x, y,
                AngleUnit.RADIANS, heading
        );

    }

    @Override
    public double getEncoderPosition() {
        return leftEnc.getPosition() + rightEnc.getPosition();
    }

    @Override
    public void reset() {
        leftEnc.reset();
        rightEnc.reset();
        imu.resetYaw();
        heading = 0.0;

        y = 0;
        x = 0;
        heading = 0;
        lastLeftEnc = 0;
        lastRightEnc = 0;
    }

    @Override
    public void setPose(Pose2D pose) {
        this.pose = pose;
        x = pose.getX(DistanceUnit.INCH);
        y = pose.getY(DistanceUnit.INCH);
        heading = pose.getHeading(AngleUnit.RADIANS);
    }

    @Override
    public Pose2D getPose() {
        return pose;
    }
}
