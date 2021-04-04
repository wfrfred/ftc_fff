package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

    /**
     * @Author RL
     * @Time 2021/4/4  20:52
     */

public class MotionModuleImpl implements MotionModule{
    private DcMotorEx leftFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightFront;
    private DcMotorEx rightBack;
    private double lFront = 0;
    private double rFront = 0;
    private double lBack = 0;
    private double rBack = 0;
    private boolean runningMode = false;
    public void changeSpeed(double anglespeed) {
        /*
        将前后平动的速度均匀分配到每个马达
        */
        this.runningMode = false;
        leftFront.setVelocity(anglespeed);
        leftBack.setVelocity(anglespeed);
        rightFront.setVelocity(anglespeed);
        rightBack.setVelocity(anglespeed);
    }
    public void turn(double anglespeed) {
        /*
        将速度均匀分配到每个马达，右侧反向
        */
        this.runningMode = false;
        leftFront.setVelocity(anglespeed);
        leftBack.setVelocity(anglespeed);
        rightFront.setVelocity(-anglespeed);
        rightBack.setVelocity(-anglespeed);
    }
    public void manSpeed(double move , double turn , double fun , double k) {
        this.runningMode = true;
        //计算每个电机
        this.lFront= (move + turn + fun)*k;
        this.lBack = (move + turn - fun)*k;
        this.rFront= (move - turn - fun)*k;
        this.rBack = (move - turn + fun)*k;

        double max=0;
        //找四个值中绝对值最大的值
        if (Math.abs(this.lFront)>Math.abs(this.lBack)){
            max = Math.abs(this.lFront);
        } else{
            max = Math.abs(this.lBack);
        }
        if (Math.abs(this.rFront)>Math.abs(max)){
            max = Math.abs(this.rFront);
        }
        if (Math.abs(this.rBack)>Math.abs(max)){
            max = Math.abs(this.rBack);
        }
        
        if (max>1){
            this.lFront = this.lFront / max;
            this.lBack  = this.lBack  / max;
            this.rFront = this.rFront / max;
            this.rBack  = this.rBack  / max;
        }
        //确保没有值越界（界为[-1,1]）
        leftFront.setPower(this.lFront);
        rightFront.setPower(this.rFront);
        leftBack.setPower(this.lBack);
        rightBack.setPower(this.rBack);
        //给每个马达设置速度
    }
    public double getState(String motor)throws IllegalArgumentException {
        if (runningMode) {
            //若手动，返回Power
            if (motor == "leftFront") {
                return lFront;
            }
            if (motor == "leftBack") {
                return lBack;
            }
            if (motor == "rightFront") {
                return rFront;
            }
            if (motor == "rightBack") {
                return rBack;
            }
        }else {
            //若程序控制，返回角速度
            if (motor == "leftFront") {
                return leftFront.getVelocity();
            }
            if (motor == "leftBack") {
                return leftBack.getVelocity();
            }
            if (motor == "rightFront") {
                return rightFront.getVelocity();
            }
            if (motor == "rightBack") {
                return rightBack.getVelocity();
            }
        }
        throw(new IllegalArgumentException());
    }
}
