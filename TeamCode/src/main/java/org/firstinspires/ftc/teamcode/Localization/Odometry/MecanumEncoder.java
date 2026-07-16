package org.firstinspires.ftc.teamcode.Localization.Odometry;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;

public class MecanumEncoder extends SubsystemBase implements Encoder {
    private final Motor.Encoder backRightEnc, backLeftEnc, frontRightEnc, frontLeftEnc;
    private final Motor fr, fl, br, bl;
    private final IMU imu;
    private Pose2D pose;
    private  double x;
    private double y;
    private double heading;


    public MecanumEncoder(Pose2D startPose, IMU imu, Motor fr, Motor fl, Motor br, Motor bl) {
       this.fr = fr;
       this.fl = fl;
       this.br = br;
       this.bl = bl;

        this.imu = imu;
        imu.resetYaw();

        backRightEnc = br.encoder;
        backLeftEnc = bl.encoder;
        frontLeftEnc = fl.encoder;
        frontRightEnc = fr.encoder;

        pose = startPose;
    }

    @Override
    public void update() {
        int leftFrontTicks = frontLeftEnc.getPosition();
        int rightFrontTicks = frontRightEnc.getPosition();
        int leftBackTicks = backLeftEnc.getPosition();
        int rightBackTicks = backRightEnc.getPosition();




    }

    @Override
    public double getEncoderPosition() {
        return frontLeftEnc.getPosition() + frontRightEnc.getPosition()
                + backLeftEnc.getPosition() + backRightEnc.getPosition();
    }

    @Override
    public void reset() {

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
        return null;
    }
}
