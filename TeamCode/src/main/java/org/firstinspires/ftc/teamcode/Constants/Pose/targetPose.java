package org.firstinspires.ftc.teamcode.Constants.Pose;

import android.graphics.RadialGradient;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public final class targetPose {
//     ----------- AUTO 1 -----------
    public static Pose2D firstPath = new Pose2D(DistanceUnit.INCH, 20, 20, AngleUnit.RADIANS, Math.toRadians(0));
    public static Pose2D secondPath = new Pose2D(DistanceUnit.INCH, -20, -20, AngleUnit.RADIANS, Math.toRadians(0));
}
