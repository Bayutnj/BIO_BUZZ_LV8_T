package org.firstinspires.ftc.teamcode.Localization;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localization.EncoderDrive.Encoder;

public  class EncoderLocalizer extends SubsystemBase implements Localizer {
    private final Encoder encoder;
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
    public void reset() {
        encoder.reset();
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
