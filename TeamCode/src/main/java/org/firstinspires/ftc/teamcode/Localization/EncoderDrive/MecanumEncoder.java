package org.firstinspires.ftc.teamcode.Localization.EncoderDrive;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class MecanumEncoder extends SubsystemBase implements Encoder {
    private final Motor.Encoder backRightEnc, backLeftEnc, frontRightEnc, frontLeftEnc;
    private Pose2D pose;

    public MecanumEncoder(Pose2D startPose) {
        Motor frontRight = new Motor();
        Motor frontLeft = new Motor();
        Motor backRight = new Motor();
        Motor backLeft = new Motor();

        backRightEnc = backRight.encoder;
        backLeftEnc = backLeft.encoder;
        frontLeftEnc = frontLeft.encoder;
        frontRightEnc = frontRight.encoder;

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
    public void reset() {

    }

    @Override
    public void setPose(Pose2D pose) {
        this.pose = pose;
    }

    @Override
    public Pose2D getPose() {
        return null;
    }
}
