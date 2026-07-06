package org.firstinspires.ftc.teamcode.Constants;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Motor.Config.motorLimit;
import org.firstinspires.ftc.teamcode.Motor.DriveTrainType;
import org.firstinspires.ftc.teamcode.Motor.Localizer;
import org.firstinspires.ftc.teamcode.Motor.Config.motorTypes;

public final class RobotConstant {
    private RobotConstant() {
    }

    public static final Alliance alliance = Alliance.BLUE;
    public static final double STARTING_HEADING = Math.toRadians(90);
    public static final Pose2D STARTING_POSE = alliance.getPose();

//    DRIVETRAIN
    public static final DriveTrainType DRIVE_TRAIN_TYPE = DriveTrainType.TANK_DRIVE;
    public static final String LEFT_MOTOR = "leftMotor";
    public static final String RIGHT_MOTOR = "rightMotor";
    public static final String FRONT_LEFT_MOTOR = "frontLeftMotor";
    public static final String FRONT_RIGHT_MOTOR = "frontRightMotor";
    public static final motorTypes DRIVE_TRAIN_MOTOR = motorTypes.CORE_HEX;
    public static final DcMotorSimple.Direction LEFT_DIRECTION =
            DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction RIGHT_DIRECTION =
            DcMotorSimple.Direction.FORWARD;

    public static final DcMotorSimple.Direction FRONT_RIGHT_DIRECTION =
            DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction FRONT_LEFT_DIRECTION =
            DcMotorSimple.Direction.REVERSE;

    public static final DcMotor.ZeroPowerBehavior DRIVETRAIN_BEHAVIOR =
            DcMotor.ZeroPowerBehavior.BRAKE;
    public static final motorLimit DRIVETRAIN_LIMIT = motorLimit.defaults();

//    DRIVE ENCODER
    public static final DcMotor.RunMode DRIVETRAIN_MODE = DcMotor.RunMode.RUN_WITHOUT_ENCODER;

    public static final double wheelDiameter = 3.54331; // INCH
    public static final double TRACK_WIDTH_IN = 14.0;
//    PINPOINT
    public static final Localizer localizer = Localizer.PINPOINT;
    public static final String localizerName = localizer.getHardware();
    public static final double xOffset = 0.0;
    public static final double yOffset = 0.0;
    public static final GoBildaPinpointDriver.GoBildaOdometryPods GO_BILDA_ODOMETRY_PODS = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
    public static final GoBildaPinpointDriver.EncoderDirection forwardDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
    public static final GoBildaPinpointDriver.EncoderDirection lateralDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;

//    INTAKE
    public static final String INTAKE = "intake";
    public static final motorTypes INTAKE_MOTOR = motorTypes.HD_HEX_2000;
    public static final DcMotorSimple.Direction INTAKE_DIRECTION =
            DcMotorSimple.Direction.REVERSE;
    public static final DcMotor.ZeroPowerBehavior INTAKE_BEHAVIOR =
            DcMotor.ZeroPowerBehavior.FLOAT;
    public static final DcMotor.RunMode INTAKE_MODE = DcMotor.RunMode.RUN_WITHOUT_ENCODER;

    public static final motorLimit INTAKE_LIMIT = new motorLimit(1.0, 4.0);

//    IMU
    public static final String IMU_NAME = "IMU";
    public static final IMU.Parameters IMU_PARAMETERS = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
            ));

}
