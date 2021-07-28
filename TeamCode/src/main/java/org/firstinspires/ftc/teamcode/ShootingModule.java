package org.firstinspires.ftc.teamcode;

/**
 * @author houyicheng2005
 * @version 1.0
 * @Time 2021-04-05 22:10
 */
public interface ShootingModule {
    /**
     * 用于发射
     * 每次发射清空弹仓
     * 次数为设定的子弹数
     */
    void shoot();
    void shoot3();
    /**
     * 用于瞄准
     */
    void aim();

    /**
     * 用于启动飞轮
     */
    void startMotor();

    /**
     * 用于停止飞轮
     */
    void stopMotor();

    boolean isMotorStarted();
}
