package org.firstinspires.ftc.teamcode.Subsystem.Drive;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Localization.Enum.DriveTrainType;
import org.firstinspires.ftc.teamcode.Localization.Localizer;

public class DriveChooser {
    public static DriveTrain create(HardwareMap hardwareMap, Localizer localizer) {
        switch (RobotConstant.DRIVE_TRAIN_TYPE) {

            case MECANUM_DRIVE:
                return new MecanumDrive(hardwareMap, localizer);

            case TANK_DRIVE:
                return new TankDrive(hardwareMap, localizer);

            default:
                throw new IllegalArgumentException(
                        "Unknown DriveTrainType"
                );
        }
    }
}
