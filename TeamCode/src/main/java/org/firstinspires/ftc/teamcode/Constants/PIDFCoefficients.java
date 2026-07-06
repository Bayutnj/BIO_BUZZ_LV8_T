package org.firstinspires.ftc.teamcode.Constants;

import com.arcrobotics.ftclib.controller.PIDFController;

public final class PIDFCoefficients {

    public static final double drive_kP = 0.03;
    public static final double drive_kI = 0;
    public static final double drive_kD = 0.00001;
    public static final double drive_kF = 0.00001;

    public static final double turn_kP = 0.03;
    public static final double turn_kI = 0;
    public static final double turn_kD = 0.00001;
    public static final double turn_kF = 0.00001;
    public static final com.qualcomm.robotcore.hardware.PIDFCoefficients flyWheel =
            new com.qualcomm.robotcore.hardware.PIDFCoefficients(0.1, 0,0.001 ,10.5);

    public static final PIDFController drivePIDF = new PIDFController(PIDFCoefficients.drive_kP,
            PIDFCoefficients.drive_kI, PIDFCoefficients.drive_kD, PIDFCoefficients.drive_kF);

    public static final PIDFController turnPIDF = new PIDFController(PIDFCoefficients.turn_kP,
            PIDFCoefficients.turn_kI, PIDFCoefficients.turn_kD, PIDFCoefficients.turn_kF);


}
