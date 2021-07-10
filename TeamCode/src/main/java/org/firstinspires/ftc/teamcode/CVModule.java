package org.firstinspires.ftc.teamcode;


public interface CVModule {

    boolean isBlocked();

    double[] getLocation();

    double getAngle();

    double getTotalAngle();

    double[] getTarget();

    int getMode();

}