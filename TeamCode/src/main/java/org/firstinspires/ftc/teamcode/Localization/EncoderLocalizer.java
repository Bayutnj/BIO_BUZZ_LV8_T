package org.firstinspires.ftc.teamcode.Localization;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localization.Odometry.Encoder;

public class EncoderLocalizer extends SubsystemBase implements Localizer {
    private Encoder encoder;

    public EncoderLocalizer(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void periodic() {
        encoder.update();
    }

    @Override
    public void setPose(Pose2D pose) {
        encoder.setPose(pose);
    }

    @Override
    public Pose2D getPose() {
        return encoder.getPose();
    }

    @Override
    public double getX() {
        return getPose().getX(DistanceUnit.INCH);
    }

    @Override
    public double getY() {
        return getPose().getY(DistanceUnit.INCH);
    }

    @Override
    public double getHeading() {
        return getPose().getHeading(AngleUnit.RADIANS);
    }

    @Override
    public void reset() {
        encoder.reset();
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
