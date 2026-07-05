package org.firstinspires.ftc.teamcode.Motor;

public enum motorTypes {

    CORE_HEX(125, 4, DcMotorConstants.CorehexGearRatio),
    HD_HEX_2000(2000, 28, DcMotorConstants.HDXHEX2000GearRatio),
    HD_HEX_6000(6000, 28, DcMotorConstants.HDHex6000GearRatio);


    private final double gearRatio;
    private final double rpm;
    private final double countPerRev;

    motorTypes(double rpm, double countPerRev, double gearRatio) {
        this.rpm = rpm;
        this.gearRatio = gearRatio;
        this.countPerRev = countPerRev;
    }

    public double ticksPerRevo() {
        return countPerRev * gearRatio;
    }
}
