package org.firstinspires.ftc.teamcode.Localization.Odometry;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public interface Encoder {
    void reset();
    void update();
    void setPose(Pose2D pose);
    double getEncoderPosition();
    Pose2D getPose();
}
