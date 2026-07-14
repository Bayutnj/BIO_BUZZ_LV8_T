package org.firstinspires.ftc.teamcode.Controller;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Subsystem.driveTrain;

public class DriveController extends SubsystemBase {
    driveTrain drive = new driveTrain();

    public void arcadeDrive(double y, double x) {
        drive.ArcadeDrive(y, x);
    }

    public void fieldCentric(double y, double x, double rx) {
        drive.fieldCentricMecanum(y, x, rx);
    }
}
