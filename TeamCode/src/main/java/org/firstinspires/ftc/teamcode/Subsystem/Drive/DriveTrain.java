package org.firstinspires.ftc.teamcode.Subsystem.Drive;

import com.arcrobotics.ftclib.command.Subsystem;

public interface DriveTrain extends Subsystem {
    void ArcadeDrive(double forward, double turn);
    void tankDrive(double leftPower, double rightPower);
    void driveRobotCentric(double forward, double lateral, double turn);
    void driveFieldCentric(double forward, double lateral, double turn);
    void stop();
    void resetMotorEncoder();
    void setBrakeMode(boolean brakeMode);
}
