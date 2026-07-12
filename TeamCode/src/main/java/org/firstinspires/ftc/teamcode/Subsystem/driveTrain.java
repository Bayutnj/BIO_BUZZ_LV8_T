package org.firstinspires.ftc.teamcode.Subsystem;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;
import org.firstinspires.ftc.teamcode.Motor.DriveTrainType;
import org.firstinspires.ftc.teamcode.Motor.Localizer;
import org.firstinspires.ftc.teamcode.Motor.motorInitialize;

public class driveTrain extends SubsystemBase {

    private DriveTrainType driveTrainType;
    private motorInitialize lm, rm;
    private motorInitialize frm, flm;
    private  Localizer localizerType = RobotConstant.localizer;
    private String localizerFallback;
    private GoBildaPinpointDriver odo;
    private IMU imu;
    private boolean overVoltage;
    private boolean usingFallback;

    private double ticksToInch;
    private double poseX, poseY, poseHeading;
    private double lastlT, lastrT;
    private double lastFlT, lastFrT;

    private ElapsedTime timeOut = new ElapsedTime();

    public driveTrain() {}
    public void init(HardwareMap hwMap, Pose2D beginPose) {
        driveTrainType = RobotConstant.DRIVE_TRAIN_TYPE;

        if (driveTrainType == DriveTrainType.TANK_DRIVE) {
            lm = new motorInitialize(hwMap, RobotConstant.LEFT_MOTOR, RobotConstant.LEFT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);

            rm = new motorInitialize(hwMap, RobotConstant.RIGHT_MOTOR, RobotConstant.RIGHT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);
        } else {
            lm = new motorInitialize(hwMap, RobotConstant.LEFT_MOTOR, RobotConstant.LEFT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);

            rm = new motorInitialize(hwMap, RobotConstant.RIGHT_MOTOR, RobotConstant.RIGHT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);

            frm = new motorInitialize(hwMap, RobotConstant.FRONT_RIGHT_MOTOR, RobotConstant.FRONT_RIGHT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);
            flm = new motorInitialize(hwMap, RobotConstant.FRONT_LEFT_MOTOR, RobotConstant.FRONT_LEFT_DIRECTION
                    , RobotConstant.DRIVETRAIN_BEHAVIOR, RobotConstant.DRIVETRAIN_MODE,
                    RobotConstant.DRIVE_TRAIN_MOTOR, RobotConstant.DRIVETRAIN_LIMIT);
        }

        if (localizerType == Localizer.PINPOINT) {
            odo = hwMap.get(GoBildaPinpointDriver.class, RobotConstant.localizerName);
            odo.setEncoderResolution(RobotConstant.GO_BILDA_ODOMETRY_PODS);
            odo.setEncoderDirections(RobotConstant.forwardDirection, RobotConstant.lateralDirection);
            odo.setOffsets(RobotConstant.xOffset, RobotConstant.yOffset, DistanceUnit.MM);
            odo.resetPosAndIMU();

            odo.setPosition(beginPose);
            odo.initialize();

            ticksToInch = (Math.PI * RobotConstant.wheelDiameter) / RobotConstant.DRIVE_TRAIN_MOTOR.getTicksPerRevolution();

            imu = hwMap.get(IMU.class, RobotConstant.IMU_NAME);
            imu.initialize(RobotConstant.IMU_PARAMETERS);

            lastlT = lm.pureTicks();
            lastrT = rm.pureTicks();

            if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                lastFlT = flm.pureTicks();
                lastFrT = frm.pureTicks();
            }

        } else {
            usingFallback = true;
            imu = hwMap.get(IMU.class, RobotConstant.IMU_NAME);
            imu.initialize(RobotConstant.IMU_PARAMETERS);

            ticksToInch = (Math.PI * RobotConstant.wheelDiameter) / RobotConstant.DRIVE_TRAIN_MOTOR.getTicksPerRevolution();
            lastlT = lm.pureTicks();
            lastrT = rm.pureTicks();

            if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                lastFlT = flm.pureTicks();
                lastFrT = frm.pureTicks();
            }

            setPosition(beginPose);

            imu.resetYaw();
        }

        PIDFCoefficients.turnPIDF.setTolerance(Math.toRadians(1));
        PIDFCoefficients.drivePIDF.setTolerance(Math.toRadians(1));
    }

    @Override
    public void periodic() {
        TelemetryPacket packet = new TelemetryPacket();

        if (driveTrainType == DriveTrainType.TANK_DRIVE) {
            overVoltage = lm.getMotor().isOverCurrent() || rm.getMotor().isOverCurrent();
        } else {
            overVoltage = lm.getMotor().isOverCurrent() || rm.getMotor().isOverCurrent()
                    || flm.getMotor().isOverCurrent() || frm.getMotor().isOverCurrent();
        }

        if (localizerType == Localizer.PINPOINT) {
            odo.update();

            GoBildaPinpointDriver.DeviceStatus DeviceStatus =  odo.getDeviceStatus();
            boolean available = DeviceStatus == GoBildaPinpointDriver.DeviceStatus.READY ||
                    DeviceStatus == GoBildaPinpointDriver.DeviceStatus.CALIBRATING;

            localizerFallback = "PINPOINT";
            if (available) {
                usingFallback = false;

                poseX = odo.getPosX(DistanceUnit.INCH);
                poseY = odo.getPosY(DistanceUnit.INCH);
                poseHeading = odo.getHeading(AngleUnit.RADIANS);

                lastlT = lm.pureTicks();
                lastrT = rm.pureTicks();
                if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                    lastFlT = flm.pureTicks();
                    lastFrT = frm.pureTicks();
                }

                localizerFallback = "PINPOINT";
            } else {
                usingFallback = true;
                updateEncoderDrive();

                localizerFallback = "ENCODER_DRIVE";
            }

        } else {
            updateEncoderDrive();
            localizerFallback = "ENCODER_DRIVE";
        }

        packet.fieldOverlay()
                .setFill("blue")
                .fillCircle(getPoseX(), getPoseY(), 4)
                .fillRect(getPoseX() - RobotConstant.robotLength / 2.0, getPoseY() - RobotConstant.robotWidth / 2.0,
                        RobotConstant.robotLength, RobotConstant.robotWidth)
                .setStroke("blue")
                .strokeLine(getPoseX(), getPoseY(),
                        getPoseX() + (RobotConstant.robotLength / 2.0) * Math.cos(getPoseHeading()),
                        getPoseY() + (RobotConstant.robotLength / 2.0) * Math.sin(getPoseHeading()));

        packet.put("Localizer Fallbacks", getLocalizerUse());
        packet.put("Localizer Chosen", localizerType);
        packet.put("Current X", poseX);
        packet.put("Current Y", poseY);
        packet.put("Current Heading", poseHeading);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    private void updateEncoderDrive() {
        poseHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);


        int currentLeftTicks = lm.pureTicks();
        int currentRightTicks = rm.pureTicks();

        double currentLeftInch = (currentLeftTicks - lastlT) * ticksToInch;
        double currentRightInch = (currentRightTicks - lastrT) * ticksToInch;
        lastlT = currentLeftTicks;
        lastrT = currentRightTicks;

        double robotCenter = (currentLeftInch + currentRightInch) / 2.0;
        double robotLateral = 0.0;

        if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
            int currentFrontLeftTicks = flm.pureTicks();
            int currentFrontRightTicks = frm.pureTicks();

            double currentFrontLeftInch = (currentFrontLeftTicks - lastFlT) * ticksToInch;
            double currentFrontRightInch = (currentFrontRightTicks - lastFrT) * ticksToInch;

            robotCenter = (currentFrontLeftInch + currentFrontRightInch + currentLeftInch + currentRightInch) / 4.0;
            robotLateral = (currentFrontLeftInch - currentFrontRightInch - currentLeftInch + currentRightInch) / 4.0;
        }

        poseX += robotCenter * Math.cos(poseHeading) - robotLateral * Math.sin(poseHeading);
        poseY += robotCenter * Math.sin(poseHeading) + robotLateral * Math.cos(poseHeading);

    }

    public String getLocalizerUse() {return localizerFallback;}
    public double getPoseHeading() {
//        return (localizerType == Localizer.PINPOINT) ? odo.getHeading(AngleUnit.RADIANS) : poseHeading;
        return usingFallback ? poseHeading : odo.getHeading(AngleUnit.RADIANS);
    }

    public double getPoseX() {
//        return (localizerType == Localizer.PINPOINT) ? odo.getPosX(DistanceUnit.INCH) : poseX;
        return usingFallback ? poseX : odo.getPosX(DistanceUnit.INCH);
    }

    public double getPoseY() {
//        return (localizerType == Localizer.PINPOINT) ? odo.getPosY(DistanceUnit.INCH) : poseY;
        return usingFallback ? poseY : odo.getPosY(DistanceUnit.INCH);
    }

    public void setPosition(Pose2D pose) {
//        poseX = pose.getX(DistanceUnit.INCH);
        setPoseX(pose.getX(DistanceUnit.INCH), DistanceUnit.INCH);
//        poseY = pose.getY(DistanceUnit.INCH);
        setPoseY(pose.getY(DistanceUnit.INCH), DistanceUnit.INCH);
//        poseHeading = pose.getHeading(AngleUnit.RADIANS);
        setHeading(pose.getHeading(AngleUnit.RADIANS), AngleUnit.RADIANS);
    }

    public void setPoseX(double posX, DistanceUnit distanceUnit) {
        poseX = DistanceUnit.INCH.fromUnit(distanceUnit, posX);
    }
    public void setPoseY(double posY, DistanceUnit distanceUnit) {
        poseY = DistanceUnit.INCH.fromUnit(distanceUnit, posY);
    }
    public void setHeading(double angle, AngleUnit angleUnit) {
        poseHeading = AngleUnit.RADIANS.fromUnit(angleUnit, angle);
    }

    public void ArcadeDrive(double forward, double rotate) {
        if (overVoltage) {
            lm.setPower(0);
            rm.setPower(0);
            return;
        }

        double leftPower = forward - rotate;
        double rightPower = forward + rotate;

        lm.setPower(leftPower);
        rm.setPower(rightPower);
    }

    public void fieldCentricMecanum(double forward, double lateral, double rx) {
        if (driveTrainType != DriveTrainType.MECANUM_DRIVE) return;
        if (overVoltage) {
            flm.setPower(0);
            frm.setPower(0);
            lm.setPower(0);
            rm.setPower(0);
            return;
        }

        double cosGyro = Math.cos(getPoseHeading());
        double sinGyro = Math.sin(getPoseHeading());

        double forwardFc = forward * cosGyro - lateral * sinGyro;
        double lateralFc = forward * sinGyro + lateral * cosGyro;
        lateralFc *= 1.1;

        robotCentricMecanum(forwardFc, lateralFc, rx);
    }

    public void robotCentricMecanum(double forward, double lateral, double rx) {
        if (driveTrainType != DriveTrainType.MECANUM_DRIVE) return;
        if (overVoltage) {
            flm.setPower(0);
            frm.setPower(0);
            lm.setPower(0);
            rm.setPower(0);
            return;
        }

        double flPower = forward + lateral + rx;
        double frPower = forward - lateral - rx;
        double blPower = forward - lateral + rx;
        double brPower = forward + lateral - rx;

        double max = Math.max(1.0, Math.max(Math.abs(flPower), Math.max(Math.abs(frPower), Math.max(Math.abs(blPower), Math.abs(brPower)))));
        flm.setPower(flPower / max);
        frm.setPower(frPower / max);
        lm.setPower(blPower / max);
        rm.setPower(brPower / max);
    }

    /**
     * Creates a function to do strafe \ Forward in Inches
     *
     * @param useStrafe returns whether it is false or not if. if it false you can only use forward/backward, while true make your robot
     *                  strafe.
     * @param headingConsidered returns heading directions wanna be the target.
     */
    public void driveTo(boolean useStrafe, double targetInch, double headingConsidered, double maxPower, double timeout, LinearOpMode opMode) {
        double currentX = getPoseX();
        double currentY = getPoseY();
        double direction = Math.signum(targetInch);

        PIDFCoefficients.drivePIDF.reset();
        timeOut.reset();

        double errorTarget = Math.abs(targetInch) - Math.abs(3.0);

        while (opMode.opModeIsActive()) {
            periodic();

            if (overVoltage) {
                lm.setPower(0);
                rm.setPower(0);
                return;
            }


            double reached = Math.hypot(getPoseX() - currentX, getPoseY() - currentY);
            if (reached >= errorTarget) break;
            if (timeOut.time() > timeout) { break;}

            double headingError = normalizeAngle(headingConsidered - getPoseHeading());

            double correction = PIDFCoefficients.drivePIDF.calculate(-headingError, 0);

            double errorInch = (Math.abs(targetInch) - reached) * direction;
            double power = Range.clip(errorInch, -maxPower, maxPower);

            if (useStrafe && driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                double rxC = Range.clip(correction, -maxPower, maxPower);
                robotCentricMecanum(0, power, rxC);
            } else if (!useStrafe && driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                double rxC = Range.clip(correction, -maxPower, maxPower);
                robotCentricMecanum(power, 0, rxC);
            } else {
                double leftPower = Range.clip(power - correction, -maxPower, maxPower);
                double rightPower = Range.clip(power + correction, -maxPower, maxPower);
                lm.setPower(leftPower);
                rm.setPower(rightPower);
            }
        }
        lm.setPower(0);
        rm.setPower(0);
        if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
            flm.setPower(0);
            frm.setPower(0);
        }
    }

    public void turnTo(double angle, double maxPower, LinearOpMode opMode) {
        PIDFCoefficients.turnPIDF.reset();

        while (opMode.opModeIsActive()) {
            periodic();

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
            if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
                flm.setPower(-power);
                frm.setPower(power);
            }
        }
        lm.setPower(0);
        rm.setPower(0);
        if (driveTrainType == DriveTrainType.MECANUM_DRIVE) {
            flm.setPower(0);
            frm.setPower(0);
        }
    }

    private double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}