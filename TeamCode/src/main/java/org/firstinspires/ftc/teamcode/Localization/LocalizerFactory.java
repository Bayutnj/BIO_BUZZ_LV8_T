package org.firstinspires.ftc.teamcode.Localization;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localization.EncoderDrive.Encoder;
import org.firstinspires.ftc.teamcode.Localization.EncoderDrive.MecanumEncoder;
import org.firstinspires.ftc.teamcode.Localization.EncoderDrive.TankDriveEncoder;
import org.firstinspires.ftc.teamcode.Localization.Enum.DriveTrainType;
import org.firstinspires.ftc.teamcode.Localization.Enum.LocalizerType;

public class LocalizerFactory {

    private LocalizerFactory() {
    }

    public static Localizer create(HardwareMap hardwareMap, LocalizerType localizerType, DriveTrainType driveType, Pose2D startPose) {

        switch (localizerType) {
            case PINPOINT:
                return new PinpointLocalizer(hardwareMap, startPose);

            case ENCODER:
                Encoder encoder;


                switch (driveType) {
                    case MECANUM_DRIVE:
                        encoder = new MecanumEncoder(startPose);
                        break;

                    case TANK_DRIVE:
                        encoder = new TankDriveEncoder(startPose);
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Unsupported DriveTrainType: " + driveType
                        );
                }

            return new EncoderLocalizer(encoder);

            default:
                throw new IllegalArgumentException(
                    "Unsupported LocalizerType:  " + localizerType
                );
        }
    }
}
