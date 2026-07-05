package org.firstinspires.ftc.teamcode.Constants;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public enum Alliance {
    BLUE(20, new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, RobotConstant.STARTING_HEADING)),
    RED(24, new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, RobotConstant.STARTING_HEADING));

    private final int id;
    private final Pose2D pose;
    Alliance(int id, Pose2D pose) {
        this.id = id;
        this.pose = pose;
    }

    public Pose2D getPose() {
        return pose;
    }
}
