package org.firstinspires.ftc.teamcode.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Motor.Config.motorLimit;
import org.firstinspires.ftc.teamcode.Motor.Config.motorTypes;

public class motorInitialize {
    private DcMotorEx motor;

    private final double maxPower;
    private final double ticksPerRevo;

    public motorInitialize(HardwareMap hardwareMap, String hardwareName, DcMotorSimple.Direction direction,
                           DcMotor.ZeroPowerBehavior behavior, DcMotor.RunMode mode, motorTypes types, motorLimit limit) {

        motor = hardwareMap.get(DcMotorEx.class, hardwareName);
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(behavior);

        this.maxPower = limit.getMaxPower();
        this.ticksPerRevo = types.getTicksPerRevolution();

        if (limit.getCurrentAlertAmps() != Double.POSITIVE_INFINITY) {
            motor.setCurrentAlert(limit.getCurrentAlertAmps(), CurrentUnit.AMPS);
        }

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(mode);
    }

    public void setPower(double power) {
        double clampedPower = Range.clip(power, -maxPower, maxPower);
        motor.setPower(clampedPower);
    }

    public int getTicksPerRevo() {
        return motor.getCurrentPosition() / (int) (ticksPerRevo);
    }

    public double getMotorRevo() {return ticksPerRevo;}
    public int pureTicks() { return motor.getCurrentPosition(); }

    public double getVelocity() {
        return motor.getVelocity();
    }

    public DcMotorEx getMotor() {
        return motor;
    }
}
