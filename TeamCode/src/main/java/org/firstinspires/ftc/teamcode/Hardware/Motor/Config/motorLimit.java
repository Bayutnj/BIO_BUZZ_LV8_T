package org.firstinspires.ftc.teamcode.Hardware.Motor.Config;

public final class motorLimit {
    private final double maxPower;
    private final double currentAlertAmps;

    public motorLimit(double maxPower, double currentAlertAmps) {
        this.maxPower = maxPower;
        this.currentAlertAmps = currentAlertAmps;
    }

    public static motorLimit defaults() {
        return new motorLimit(1.0, Double.POSITIVE_INFINITY);
    }

    public double getMaxPower() {
        return maxPower;
    }

    public double getCurrentAlertAmps() {
        return currentAlertAmps;
    }
}