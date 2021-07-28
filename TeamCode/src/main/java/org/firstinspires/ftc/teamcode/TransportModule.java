package org.firstinspires.ftc.teamcode;

/**
 * 用于机器人的传送带运输
 *
 * @author wfrfred
 * @version 1.0
 * @Time 2021-07-06 19:06
 */
public interface TransportModule {
    void startTransporting(boolean isForward);

    void stopTransporting();
}
