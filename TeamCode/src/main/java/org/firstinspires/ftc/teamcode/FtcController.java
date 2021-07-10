package org.firstinspires.ftc.teamcode;

/**
 * 统筹管理调用各类机器人模块
 *
 * @author wfrfred
 * @version 1.1
 * @Time 2021-04-05 12:38
 */

public interface FtcController {

    /**
     * 启动手柄按键监听
     */
    void startGamepadListening() throws NullPointerException;

    void setIsListening(boolean isListening);

    boolean getIsListening();

    double getTotalAngle();

}
