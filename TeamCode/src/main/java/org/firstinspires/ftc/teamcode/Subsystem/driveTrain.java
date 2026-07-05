package org.firstinspires.ftc.teamcode.Subsystem;


import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants.Alliance;
import org.firstinspires.ftc.teamcode.Constants.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Motor.Localizer;
import org.firstinspires.ftc.teamcode.Motor.motorInitialize;

public class driveTrain {
    private motorInitialize lm, rm;
    private final Localizer localizerType = RobotConstant.localizer;
    private GoBildaPinpointDriver odo;
    private IMU imu;
    private boolean overVoltage;

    private double ticksToInch;
    private double poseX, poseY, poseHeading;
    private double lastlT, lastrT;

    public void init(HardwareMap hwMap) {
        lm = new motorInitialize(hwMap, RobotConstant.LEFT_MOTOR, RobotConstant.LEFT_DIRECTION
                , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);

        rm = new motorInitialize(hwMap, RobotConstant.RIGHT_MOTOR, RobotConstant.RIGHT_DIRECTION
                , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);

        if (localizerType == Localizer.PINPOINT) {
            odo = hwMap.get(GoBildaPinpointDriver.class, RobotConstant.localizerName);
            odo.setEncoderResolution(RobotConstant.GO_BILDA_ODOMETRY_PODS);
            odo.setEncoderDirections(RobotConstant.forwardDirection, RobotConstant.lateralDirection);
            odo.setOffsets(RobotConstant.xOffset, RobotConstant.yOffset, DistanceUnit.MM);
            odo.resetPosAndIMU();

            odo.setPosition(RobotConstant.STARTING_POSE);
            odo.initialize();
        } else {
            imu = hwMap.get(IMU.class, RobotConstant.IMU_NAME);
            imu.initialize(RobotConstant.IMU_PARAMETERS);

            ticksToInch = (Math.PI * RobotConstant.wheelDiameter) / RobotConstant.DRIVE_TRAIN_MOTOR.ticksPerRevo();
            lastlT = lm.pureTicks();
            lastrT = rm.pureTicks();

            imu.resetYaw();
        }

        PIDFCoefficients.turnPIDF.setTolerance(Math.toRadians(1));
        PIDFCoefficients.drivePIDF.setTolerance(Math.toRadians(1));
    }

    public void update() {
        overVoltage = lm.getMotor().isOverCurrent() || rm.getMotor().isOverCurrent();

        if (localizerType == Localizer.PINPOINT) {
            odo.update();
        } else {
            updateEncoderDrive();
        }
    }

    private void updateEncoderDrive() {
        int currentLeftTicks = lm.pureTicks();
        int currentRightTicks = rm.pureTicks();

        double currentLeftInch = (currentLeftTicks - lastlT) * ticksToInch;
        double currentRightInch = (currentRightTicks - lastrT) * ticksToInch;
        lastlT = currentLeftTicks;
        lastrT = currentRightTicks;

        double robotCenter = (currentLeftInch + currentRightInch) / 2.0;

        poseHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + RobotConstant.STARTING_HEADING;
        poseX += robotCenter * Math.cos(poseHeading);
        poseY += robotCenter * Math.sin(poseHeading);

    }

    public double getPoseHeading() {
        return (localizerType == Localizer.PINPOINT) ? odo.getHeading(AngleUnit.RADIANS) : poseHeading;
    }

    public double getPoseX() {
        return (localizerType == Localizer.PINPOINT) ? odo.getPosX(DistanceUnit.INCH) : poseX;
    }

    public double getPoseY() {
        return (localizerType == Localizer.PINPOINT) ? odo.getPosY(DistanceUnit.INCH) : poseY;
    }

    public void manualDrive(double forward, double rotate) {
        double leftPower = forward - rotate;
        double rightPower = forward + rotate;

        lm.setPower(leftPower);
        rm.setPower(rightPower);
    }

    public void driveTo(double targetInch, double headingConsidered, double maxPower, LinearOpMode opMode) {
        double currentX = getPoseX();
        double currentY = getPoseY();
        double direction = Math.signum(targetInch);

        PIDFCoefficients.drivePIDF.reset();

        while (opMode.opModeIsActive()) {
            update();

            if (overVoltage) {
                lm.setPower(0);
                rm.setPower(0);
                return;
            }

            double reached = Math.hypot(getPoseX() - currentX, getPoseY() - currentY);
            if (reached >= Math.abs(targetInch)) break;

            double headingError = normalizeAngle(headingConsidered - getPoseHeading());

            double correction = PIDFCoefficients.drivePIDF.calculate(-headingError, 0);

            double errorInch = (Math.abs(targetInch) - reached) * direction;
            double power = Range.clip(errorInch, -maxPower, maxPower);

            lm.setPower(power - correction);
            rm.setPower(power + correction);
        }
        lm.setPower(0);
        rm.setPower(0);
    }

    public void turnTo(double angle, double maxPower, LinearOpMode opMode) {
        PIDFCoefficients.turnPIDF.reset();

        while (opMode.opModeIsActive()) {
            update();

            if (overVoltage) {
                lm.setPower(0);
                rm.setPower(0);
                return;
            }

            double headingError = normalizeAngle(angle - getPoseHeading());
            if (Math.abs(headingError) <= PIDFCoefficients.turnPIDF.getTolerance()[0]) break;

            double power = PIDFCoefficients.turnPIDF.calculate(-headingError, 0);
            power = Range.clip(power, -maxPower, maxPower);

            lm.setPower(-power);
            rm.setPower(power);
        }
        lm.setPower(0);
        rm.setPower(0);
    }

    public DcMotorEx getLeftMotor() {
        return lm.getMotor();
    }

    public DcMotorEx getRightMotor() {
        return rm.getMotor();
    }

    private double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
