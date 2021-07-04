package org.firstinspires.ftc.teamcode;

/**
 * 底盘相关最基础方法
 * 包含平动转动等
 *
 * @author RL
 * @Time 2021/4/4  20:52
 */

public interface MotionModule{
    /**
     * 控制电机转动
     * @param motor 选择电机
     * @param angleSpeed 设定的转速
     */
    void setSpeed(int motor,double angleSpeed);
    /**
     *移动一定距离
     * @param distance
     */
    void move(double distance,boolean isForward);

    /**
     *转动一定角度
     * @param angle
     */
    void turn(double angle);

    /**
      * 人使用摇杆操作机器人
      * @param move 前后平移，正方向为前
      * @param turn 原地转动，由于数据限制，角速原地旋转，正方向为顺时针
      * @param fun  左右平移，正方向为左
      * @param k    速度系数
      * 三个变量范围均为[-1,1]
      * 需注意的是，如果这几个系数在运算后某一电机的速度超过了最大值
      * 所有电机的转速都会被等比例缩小以保证最终机器人方向正确
      */
    void moveGamepad(double move, double turn, double fun, double k);

}
