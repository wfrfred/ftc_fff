package org.firstinspires.ftc.teamcode;

public class FtcControllerImpl implements FtcController{
    /**
     *
     */
    private MotionModuleImpl m;
    private ShootingModuleImpl s;
    private TransportModuleImpl t;
    private RoboticArmModuleImpl a;

    FtcControllerImpl(MotionModuleImpl m,ShootingModuleImpl s,TransportModuleImpl t,RoboticArmModuleImpl a){
        this.m = m;
        this.s = s;
        this.t = t;
        this.a = a;
    }
}
