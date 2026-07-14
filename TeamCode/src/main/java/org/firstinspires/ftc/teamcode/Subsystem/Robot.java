package org.firstinspires.ftc.teamcode.Subsystem;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Localization.Localizer;
import org.firstinspires.ftc.teamcode.Localization.LocalizerChooser;
import org.firstinspires.ftc.teamcode.Subsystem.Drive.DriveChooser;
import org.firstinspires.ftc.teamcode.Subsystem.Drive.DriveTrain;

public class Robot{

    private final HardwareMap hardwareMap;
    private final IMU imu;
    private final Localizer localizer;
    private final DriveTrain driveTrain;
    private final Intake intake;

    public Robot(HardwareMap hardwareMap, Pose2D startPose) {
        this.hardwareMap = hardwareMap;

        imu = hardwareMap.get(IMU.class, RobotConstant.IMU_NAME);

        localizer = LocalizerChooser.create(
                hardwareMap, imu, startPose
        );

        driveTrain = DriveChooser.create(
                hardwareMap, localizer
        );

        intake = new Intake();
        intake.init(hardwareMap);

    }

    public void periodic() {
        localizer.periodic();
        driveTrain.periodic();
        intake.periodic();
    }

    public Localizer getLocalizer() {
        return localizer;
    }

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public IMU getImu() {
        return imu;
    }

    public void setIntake(Intake.intakeState s) {
        intake.setState(s);
    }
}
