package org.firstinspires.ftc.teamcode.Motor.Config;

import org.firstinspires.ftc.teamcode.Motor.DcMotorConstants;

public enum motorTypes {

    CORE_HEX(125, 4, DcMotorConstants.CORE_HEX_GEAR_RATIO),
    HD_HEX_2000(2000, 28, DcMotorConstants.HDX_2000_RPM_GEAR_RATIO),
    HD_HEX_6000(6000, 28, DcMotorConstants.HDX_6000_RPM_GEAR_RATIO),
    GOBILDA_435(435, 28, DcMotorConstants.GOBILDA_435_GEAR_RATIO);


    private final double gearRatio;
    private final double rpm;
    private final double cpr;

    motorTypes(double rpm, double cpr, double gearRatio) {
        this.rpm = rpm;
        this.gearRatio = gearRatio;
        this.cpr = cpr;
    }

    public double getMaxVel() { return cpr * rpm / 60;}

    public double getTicksPerRevolution() {
        return cpr * gearRatio;
    }
}
