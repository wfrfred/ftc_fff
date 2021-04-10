package org.firstinspires.ftc.teamcode;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-04-05 12:38
 * @version 1.1
 */

public interface FtcController {

    /**
     * 启动摇杆操控运动模式
     * @param isMotionModuleManual
     * @warning 在操作电机时请将此设置为false，否则将被阻塞
     */
    void setMotionModuleManual(boolean isMotionModuleManual);

    /**
     * @see #setMotionModuleManual(boolean)
     * @return 返回是否为手动模式
     */
    boolean isMotionModuleManual();

    /**
     * 启动手柄按键监听
     */
    void gamepadStartListen();

    /**
     * 关闭手柄按键监听
     */
    void gamepadStopListen();
}
