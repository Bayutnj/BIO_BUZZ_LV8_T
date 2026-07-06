package org.firstinspires.ftc.teamcode.Motor;

public enum DriveTrainType {
    TANK_DRIVE (2),
    MECANUM_DRIVE (4);

    private final double motorUse;
    DriveTrainType(double motorUse) {
        this.motorUse = motorUse;
    }

}
