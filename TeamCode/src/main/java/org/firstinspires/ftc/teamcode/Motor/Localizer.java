package org.firstinspires.ftc.teamcode.Motor;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;

public enum Localizer {

    PINPOINT(RobotConstant.localizerName),
    DRIVE_ENCODER("");

    private final String name;

    Localizer(String name) {
        this.name = name;
    }

    public String getHardware() {
        return name;
    }
}
