package org.firstinspires.ftc.teamcode;

/**
 * 底盘相关最基础方法
 * 包含平动转动等
 *
 * @author RL
 * @Time 2021/4/4  20:52
 */

public interface MotionModule {


    /**
     * 机器人前后移动
     * @param angleSpeed 角速度 单位rad/s 范围由电机能力决定
     */
    void setSpeed(double angleSpeed);

    /**
     * 机器人原地转动，由于数据限制，角速度为马达转动的角速度
     * @param angleSpeed 马达转动的角速度    单位rad/s范围由电机能力决定
     */
    void turn(double angleSpeed);

    /**
      * 人使用摇杆操作机器人
      * @param move 前后平移，正方向为前
      * @param turn 原地旋转，正方向为顺时针
      * @param fun  左右平移，正方向为左
      * @param k    速度系数
      * 三个变量范围均为[-1,1]
      * 需注意的是，如果这几个系数在运算后某一电机的速度超过了最大值
      * 所有电机的转速都会被等比例缩小以保证最终机器人方向正确
      */
    public void moveGamepad(double move, double turn, double fun, double k);


    /**
     * 获得某一电机状态 根据操作模式不同有不同返回值
     * @param motor 为:
     *              leftFront左前马达
     *              leftBack左后马达
     *              rightFront右前马达
     *              rightBack右后马达
     * @return 返回角速度或人工控制的Power
     * @exception IllegalArgumentException 若参数不属于上述范围，则抛出非法参数异常
     * tips:作者估计不会有人在手动操作时获取速度
     */
    public double getState(String motor) throws IllegalArgumentException;
}
