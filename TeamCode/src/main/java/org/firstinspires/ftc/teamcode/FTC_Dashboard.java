package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.Constants.RobotConstant;

public class FTC_Dashboard {
    private FtcDashboard ftcDashboard;
    private TelemetryPacket packet;

    public FTC_Dashboard() {
//        ftcDashboard = new FtcDashboard().getInstance();
        packet = new TelemetryPacket();
    }

    public void sendFieldPose(double poseX, double poseY, TelemetryPacket packet) {
        this.packet = packet;
        packet.fieldOverlay()
                .fillCircle(poseX, poseY, 4)
                .fillRect(poseX, poseY, RobotConstant.robotWidth, RobotConstant.robotLength);

        ftcDashboard.sendTelemetryPacket(packet);
    }
}
