package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import java.util.Properties;

    /**
     * @author RL
     * @Time 2021/4/4  20:52
     * @version 1.0
     */

public class MotionModuleImpl implements MotionModule{
    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    private double lFront = 0;
    private double rFront = 0;
    private double lBack = 0;
    private double rBack = 0;
    private boolean runningMode = false;
    private double maxAngleSpeed = 3600;

    MotionModuleImpl(HardwareMap hardwareMap){
        leftFront = hardwareMap.dcMotor.get("l1");
        leftBack = hardwareMap.dcMotor.get("l2");
        rightFront = hardwareMap.dcMotor.get("r1");
        rightBack = hardwareMap.dcMotor.get("r2");
    }

    public void setSpeed(double angleSpeed) {
        /*
        将前后平动的速度均匀分配到每个马达
        */
        angleSpeed /= maxAngleSpeed;
        runningMode = false;
        leftFront.setPower(angleSpeed);
        leftBack.setPower(angleSpeed);
        rightFront.setPower(angleSpeed);
        rightBack.setPower(angleSpeed);
    }
    public void turn(double angleSpeed) {
        /*
        将速度均匀分配到每个马达，右侧反向
        */
        runningMode = false;
        leftFront.setPower(angleSpeed);
        leftBack.setPower(angleSpeed);
        rightFront.setPower(-angleSpeed);
        rightBack.setPower(-angleSpeed);
    }
    public void moveGamepad(double move , double turn , double fun , double k) {
        runningMode = true;
        //计算每个电机
        this.lFront= (move + turn + fun)*k;
        this.lBack = (move + turn - fun)*k;
        this.rFront= (move - turn - fun)*k;
        this.rBack = (move - turn + fun)*k;

        //找四个值中绝对值最大的值
        double max = Math.max(Math.abs(this.lFront), Math.abs(this.lBack));
        max = Math.max(max,Math.abs(this.rFront));
        max = Math.max(max,Math.abs(this.rBack));

        if (max>1){
            this.lFront /= max;
            this.lBack  /= max;
            this.rFront /= max;
            this.rBack  /= max;
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
            if (motor.equals("leftFront")) {
                return lFront;
            }else if (motor.equals("leftBack")) {
                return lBack;
            }else if (motor.equals("rightFront")) {
                return rFront;
            }else if (motor.equals("rightBack")) {
                return rBack;
            }
        }else {
            //若程序控制，返回角速度
            if (motor.equals("leftFront")) {
                return leftFront.getPower();
            }else if (motor.equals("leftBack")) {
                return leftBack.getPower();
            }else if (motor.equals("rightFront")) {
                return rightFront.getPower();
            }else if (motor.equals("rightBack")) {
                return rightBack.getPower();
            }
        }
        throw(new IllegalArgumentException());
    }
}
