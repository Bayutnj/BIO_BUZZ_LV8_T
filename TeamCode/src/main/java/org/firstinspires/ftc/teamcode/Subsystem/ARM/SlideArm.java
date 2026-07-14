package org.firstinspires.ftc.teamcode.Subsystem.ARM;


import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware.Motor.motorInitialize;

public class SlideArm extends SubsystemBase {
    private motorInitialize ArmM;
    private ElevatorFeedforward feedforward;
    private PIDFController pidfController;

    public SlideArm() {}

    public void init(HardwareMap HwMap) {

        feedforward = new ElevatorFeedforward(1, 1, 1);
        pidfController = new PIDFController(1, 1,1,1);
    }

    private double getCurrentInches() {
        return ArmM.pureTicks() / ArmM.getMotorRevo() * Math.PI;
    }
    public void doArm(double inch) {
        double target = Range.clip(inch, 0, 20);
        double currentInch = getCurrentInches();

        double ff = feedforward.calculate(0 ,0);
        double pidf = pidfController.calculate(currentInch, target);

        ArmM.setPower(ff + pidf);
    }

    @Override
    public void periodic() {
//        switch () {
//            case :
                doArm(13);
//        }
    }
}
