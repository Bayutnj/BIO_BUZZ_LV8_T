package org.firstinspires.ftc.teamcode.Localization;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstant;

// These class system maintain the pinpoint
public class PinpointLocalizer extends SubsystemBase implements Localizer {
    private GoBildaPinpointDriver pinpoint;
    private Pose2D pose;
    private GoBildaPinpointDriver.DeviceStatus deviceStatus;

    public PinpointLocalizer(HardwareMap hardwareMap, Pose2D startPose) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, RobotConstant.localizerName);
        pinpoint.setEncoderResolution(RobotConstant.GO_BILDA_ODOMETRY_PODS);
        pinpoint.setEncoderDirections(RobotConstant.forwardDirection, RobotConstant.lateralDirection);
        pinpoint.setOffsets(RobotConstant.xOffset, RobotConstant.yOffset, DistanceUnit.MM);

        pinpoint.resetPosAndIMU();

        pinpoint.initialize();
        pinpoint.setPosition(startPose);

        pose = startPose;

        deviceStatus = GoBildaPinpointDriver.DeviceStatus.NOT_READY;
    }

    @Override
    public void periodic() {
        pinpoint.update();

        deviceStatus = pinpoint.getDeviceStatus();

        if (isReady()) {
            pose = pinpoint.getPosition();
        }
    }

    @Override
    public void setPose(Pose2D pose) {
        pinpoint.setPosition(pose);

        this.pose = pose;
    }

    @Override
    public Pose2D getPose() {
        return pose;
    }

    @Override
    public double getX() {
        return pose.getX(DistanceUnit.INCH);
    }
    @Override
    public double getY() {
        return pose.getY(DistanceUnit.INCH);
    }
    @Override
    public double getHeading() {
        return pose.getHeading(AngleUnit.RADIANS);
    }

    @Override
    public void reset() {
        pinpoint.resetPosAndIMU();

        pose = new Pose2D(
                DistanceUnit.INCH,
                0, 0,
                AngleUnit.RADIANS, 0
        );
    }

    @Override
    public boolean isReady() {
        return deviceStatus == GoBildaPinpointDriver.DeviceStatus.READY;
    }
}
