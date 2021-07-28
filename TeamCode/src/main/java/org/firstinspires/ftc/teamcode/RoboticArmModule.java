package org.firstinspires.ftc.teamcode;

/**
 * @author gg233gg
 * @version 1.1
 * @Time 2021-05-08 16:58
 */

public interface RoboticArmModule {

    boolean ARM_UP = true;
    boolean ARM_DOWN = false;


    void liftArm();
    void putDownArm(double angle);


    /**
     * 抓取物品
     * 只会在手臂放下时执行
     */
    void grab();

    /**
     * 放开物品
     * 只会在手臂放下时执行
     */
    void release();

    /**
     * 线程安全
     *
     * @return 返回是否抓住物品
     */
    boolean isGrabbed();

    /**
     * 线程安全
     *
     * @return 返回手臂状态
     */
    boolean getArmStatus();

}
