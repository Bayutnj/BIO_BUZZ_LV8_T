package org.firstinspires.ftc.teamcode.Controller;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

public class MotionController extends SubsystemBase {
    driveTrain drive = new driveTrain();

    public boolean driveTo(Pose2D targetPose, Pose2D currentPos, double holdTime) {
        return drive.driveTo(targetPose, currentPos, holdTime);
    }
}
