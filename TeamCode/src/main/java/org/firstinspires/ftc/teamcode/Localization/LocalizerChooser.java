package org.firstinspires.ftc.teamcode.Localization;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Localization.Odometry.Encoder;
import org.firstinspires.ftc.teamcode.Localization.Odometry.MecanumEncoder;
import org.firstinspires.ftc.teamcode.Localization.Odometry.TankDriveEncoder;
import org.firstinspires.ftc.teamcode.Localization.Enum.DriveTrainType;
import org.firstinspires.ftc.teamcode.Localization.Enum.LocalizerType;
import org.firstinspires.ftc.teamcode.Subsystem.Drive.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystem.Drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystem.Drive.TankDrive;

public class LocalizerChooser {

    public static Localizer create(HardwareMap hardwareMap, Pose2D startPose) {
        IMU imu = hardwareMap.get(IMU.class, RobotConstant.IMU_NAME);
        imu.initialize(RobotConstant.IMU_PARAMETERS);
        imu.resetYaw();

        switch (RobotConstant.localizerType) {
            case PINPOINT:
                return new PinpointLocalizer(hardwareMap, startPose);

            case ENCODER:
                Encoder encoder;


                switch (RobotConstant.DRIVE_TRAIN_TYPE) {
                    case MECANUM_DRIVE:
                        encoder = new MecanumEncoder(startPose, imu, MecanumDrive.rightFront,
                                MecanumDrive.leftFront, MecanumDrive.rightBack, MecanumDrive.leftBack);
                        break;

                    case TANK_DRIVE:
                        encoder = new TankDriveEncoder(startPose, imu, TankDrive.left, TankDrive.right);
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Unsupported DriveTrainType: " + RobotConstant.DRIVE_TRAIN_TYPE
                        );
                }

            return new EncoderLocalizer(encoder);

            default:
                throw new IllegalArgumentException(
                    "Unsupported LocalizerType:  " + RobotConstant.localizerType
                );
        }
    }
}
