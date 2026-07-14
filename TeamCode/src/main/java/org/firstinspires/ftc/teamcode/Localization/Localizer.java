package org.firstinspires.ftc.teamcode.Localization;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public interface Localizer extends Subsystem {

    void init(HardwareMap hardwareMap, Pose2D startPose);
    void reset();
    void setPose(Pose2D pose);
    Pose2D getPose();
    double getX();
    double getY();
    double getHeading();
    boolean isReady();
}
