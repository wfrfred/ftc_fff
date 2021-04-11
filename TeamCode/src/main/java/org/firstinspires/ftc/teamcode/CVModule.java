package org.firstinspires.ftc.teamcode;

import android.util.Pair;

public interface CVModule {

    /**
     *
     */
    boolean isBlocked();

    Pair<Double, Double> getLocation();

    double getCAngle();

    Pair<Double, Double> getTarget();

    int getMode();

}