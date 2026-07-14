package org.firstinspires.ftc.teamcode.Localization.EncoderDrive;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class TankDriveEncoder extends SubsystemBase implements Encoder {
    private final  Motor.Encoder leftEnc, rightEnc;
    private double lastLeftEnc, lastRightEnc;
    private IMU imu;
    private Pose2D pose;

    private  double x;
    private double y;
    private double heading;
    private double ticksPerInch;

    public TankDriveEncoder(Pose2D startPose) {
        Motor leftMotor = new Motor();
        Motor rightMotor = new Motor();

        leftEnc = leftMotor.encoder;
        rightEnc = rightMotor.encoder;



        pose = startPose;
        x = pose.getX(DistanceUnit.INCH);
        y = pose.getY(DistanceUnit.INCH);
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + pose.getHeading(AngleUnit.RADIANS);
    }

    @Override
    public void update() {
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

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
    public void reset() {
        leftEnc.reset();
        rightEnc.reset();

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
