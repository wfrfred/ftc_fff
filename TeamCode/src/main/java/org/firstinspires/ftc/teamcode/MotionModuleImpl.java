package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
     * @author RL
     * @Time 2021/4/4  20:52
     * @version 1.0
     */

public class MotionModuleImpl implements MotionModule{
    private DcMotor[] dcMotors = new DcMotor[4]; //左前，左后，右前，右后

    private volatile double lFront = 0;
    private volatile double rFront = 0;
    private volatile double lBack = 0;
    private volatile double rBack = 0;
    private final AtomicBoolean runningMode = new AtomicBoolean(false);//是否手动操作
    private final double MAX_ANGLE_SPEED = 3600;//@warning

    MotionModuleImpl(HardwareMap hardwareMap){
        dcMotors[1] = hardwareMap.dcMotor.get("l1");
        dcMotors[2] = hardwareMap.dcMotor.get("l2");
        dcMotors[3] = hardwareMap.dcMotor.get("r1");
        dcMotors[4] = hardwareMap.dcMotor.get("r2");
    }

    public void setSpeed(double angleSpeed) {
        /*
        将前后平动的速度均匀分配到每个马达
        */
        angleSpeed /= MAX_ANGLE_SPEED;
        synchronized (dcMotors) {
            runningMode.set(false);
            dcMotors[1].setPower(angleSpeed);
            dcMotors[2].setPower(angleSpeed);
            dcMotors[3].setPower(angleSpeed);
            dcMotors[4].setPower(angleSpeed);
        }
    }
    public void turn(double angleSpeed) {
        /*
        将速度均匀分配到每个马达，右侧反向
        */
        angleSpeed /= MAX_ANGLE_SPEED;
        synchronized (dcMotors) {
            runningMode.set(false);
            dcMotors[1].setPower(angleSpeed);
            dcMotors[2].setPower(angleSpeed);
            dcMotors[3].setPower(-angleSpeed);
            dcMotors[4].setPower(-angleSpeed);
        }
    }
    public void moveGamepad(double move , double turn , double fun , double k) {
        double lFront;
        double lBack;
        double rFront;
        double rBack;

        //计算每个电机
        lFront= (move + turn + fun)*k;
        lBack = (move + turn - fun)*k;
        rFront= (move - turn - fun)*k;
        rBack = (move - turn + fun)*k;
        //找四个值中绝对值最大的值
        double max = Math.max(Math.abs(this.lFront), Math.abs(this.lBack));
        max = Math.max(max,Math.abs(this.rFront));
        max = Math.max(max,Math.abs(this.rBack));

        if (max>1){
            lFront /= max;
            lBack  /= max;
            rFront /= max;
            rBack  /= max;
        }
        synchronized (dcMotors) {
            this.lBack = lBack;
            this.lFront = lFront;
            this.rBack = rBack;
            this.rFront = rFront;
            runningMode.set(true);
            //确保没有值越界（界为[-1,1]）
            dcMotors[1].setPower(lFront);
            dcMotors[2].setPower(rFront);
            dcMotors[3].setPower(lBack);
            dcMotors[4].setPower(rBack);
            //给每个马达设置速度
        }
    }
    public double getState(String motor)throws IllegalArgumentException {
        if(runningMode.get()==true){
            switch(motor){

            }
        }
        throw(new IllegalArgumentException());
    }
}
