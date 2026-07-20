package org.firstinspires.ftc.teamcode.Robot_Main.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Shooter;

@TeleOp
public class ShooterTest extends OpMode {
    private final Shooter shooter = new Shooter();

    @Override
    public void init() {
        shooter.init(hardwareMap);
        shooter.setDistanceGoal(60); // INCHE's
    }

    @Override
    public void loop() {
        shooter.periodic();

        if (gamepad1.y) {
            shooter.setState(Shooter.sState.SPINS);
        } else {
            shooter.setState(Shooter.sState.STOP);
        }

        telemetry.addData("TARGET VEL", shooter.targetVelocity());
        telemetry.addData("CURRENT VEL", shooter.getVelocity());
        telemetry.update();
    }
}
