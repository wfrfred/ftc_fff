package org.firstinspires.ftc.teamcode;

public interface MotionModule {
    private DcMotorEx leftFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightFront;
    private DcMotorEx rightBack;
    private double lFront;
    private double rFront;
    private double lBack;
    private double rBack;
    private boolean runningMode;
    /*
    底盘相关最基础方法
    包含平动转动等

    leftFront左前马达
    leftBack左后马达
    rightFront右前马达
    rightBack右后马达

    lFront左前马达Power
    rFront右前马达Power
    lBack左后马达Power
    rBack右后马达Power

    runningMode是当前机器人运转模式
    true为Power(手动操作)模式
    false为anglespeed(程序操作)模式
    该变量会改变getState的返回值，若为true则返回Power值,false则返回角速度值

    @Author RL
    @Time 2021/4/4  20:52
    */
    public void changeSpeed(double anglespeed) {
        /*
        @see
        机器人前后移动

        @param anglespeed 角速度    单位rad/s
        范围由电机能力决定
        */
    }
    public void turn(double anglespeed) {
        /*
        @see
        机器人原地转动，由于数据限制，角速度为马达转动的角速度

        @param anglespeed 马达转动的角速度    单位rad/s
        范围由电机能力决定
        */
    }
    public void manSpeed(double move , double turn , double fun , double k) {
        /*
        @see
        人使用摇杆操作机器人

        @param
        move前后平移，正方向为前
        turn原地旋转，正方向为顺时针
        fun 左右平移，正方向为左
        三个变量范围均为[-1,1]
        k为系数，这三个变量的每一个都会乘这个系数

        需注意的是，如果这几个系数在运算后某一电机的速度超过了最大值
        所有电机的转速都会被等比例缩小以保证最终机器人方向正确
        */
    }
    public double getState(String motor){
        /*
        @see
        获得某一电机状态

        @param
        motor为
            leftFront左前马达
            leftBack左后马达
            rightFront右前马达
            rightBack右后马达

        @return
        返回角速度或人工控制的Power

        tips:作者估计不会有人在手动操作时获取速度
         */
    }
}
