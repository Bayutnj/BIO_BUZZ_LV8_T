package org.firstinspires.ftc.teamcode.Motor;

public class BangBangController {

    private double tolerance;
    private double setPoint;
    private double pv;
    public BangBangController(double tolerance) {
        this.tolerance = tolerance;
    }

    public double calculate(double pv, double sp) {
        this.setPoint = sp;
        this.pv = pv;
        return pv < sp ? 1.0 : 0.0;
    }

    public boolean atSetPoint() {
        return Math.abs(setPoint - pv) < tolerance;
    }

    public void setSetPoint(double sp) {
        this.setPoint = sp;
    }

    public double getError() {
        return setPoint - pv;
    }
}
