package org.firstinspires.ftc.teamcode;

/**
 * @author gg233gg
 * @version 1.1
 * @Time 2021-05-08 16:58
 */

public interface RoboticArmModule {

    boolean ARM_UP = true;
    boolean ARM_DOWN = false;

    /**
     * 使得机器人到达目标物位置
     *
     * @param motionModule 将运动模块移交给机械爪
     */
    void goToTarget(MotionModule motionModule);

    /**
     * 抬起手臂
     */
    void liftArm();

    /**
     * 放下手臂
     */
    void putDownArm();

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
