package org.firstinspires.ftc.teamcode;

/**
 * @author houyicheng2005
 * @Time 2021-04-05 22:10
 * @version 1.0
 */
public interface ShootingModule {
    /**
     * 用于发射
     * 每次发射清空弹仓
     * 次数为设定的子弹数
     */
    void shoot();

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

    /**
     * 获取子弹数
     * @return 子弹数
     */
    int getBulletAmount();

    /**
     * 改变子弹数
     * @param bulletAmount 要设定的子弹数 范围为0到3
     */
    void setBulletAmount(int bulletAmount);
}
