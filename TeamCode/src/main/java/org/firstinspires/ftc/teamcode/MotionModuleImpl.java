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
    private final AtomicBoolean runningMode = new AtomicBoolean(false);//是否手动操作
    private final double MAX_ANGLE_SPEED = 3600;//@warning
    private CVModule cvModule;
    public final double[] AREA_SIZE = {100,100};

    public void setSpeed(int motor,double angleSpeed){
        dcMotors[motor].setPower(angleSpeed/MAX_ANGLE_SPEED);
    }

    public void move(double distance,boolean isForward){
        /*
        double[] location = cvModule.getLocation();
        double angle = cvModule.getAngle();
        double[] destination ={location[0]+distance*Math.sin(angle),location[1]+distance*Math.cos(angle)};
        if(destination[0]>AREA_SIZE[0]||destination[1]>AREA_SIZE[1]|| destination[0]<0||destination[1]<0){return;}
        if (isForward) {
            moveGamepad(1, 0, 0, 1);
        } else {
            moveGamepad(0, 0, 1, 1);
        }

         */
    }

    public void turn(double angleSpeed){

    }

    MotionModuleImpl(HardwareMap hardwareMap,CVModule cvModule){
        dcMotors[0] = hardwareMap.dcMotor.get("l1");
        dcMotors[1] = hardwareMap.dcMotor.get("l2");
        dcMotors[2] = hardwareMap.dcMotor.get("r1");
        dcMotors[3] = hardwareMap.dcMotor.get("r2");
        this.cvModule = cvModule;
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
        double max = Math.max(Math.abs(lFront), Math.abs(lBack));
        max = Math.max(max,Math.abs(rFront));
        max = Math.max(max,Math.abs(rBack));

        if (max>1){
            lFront /= max;
            lBack  /= max;
            rFront /= max;
            rBack  /= max;
        }
        synchronized (dcMotors) {
            runningMode.set(true);
            //确保没有值越界（界为[-1,1]）
            dcMotors[0].setPower(lFront);
            dcMotors[1].setPower(rFront);
            dcMotors[2].setPower(lBack);
            dcMotors[3].setPower(rBack);
            //给每个马达设置速度
        }
    }

}
