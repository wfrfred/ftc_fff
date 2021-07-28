package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.CVModule;

import java.util.concurrent.atomic.AtomicBoolean;

import org.firstinspires.ftc.teamcode.MotionModule;
import com.fff.util.pid.*;

/**
 * @author RL
 * @version 1.0
 * @Time 2021/4/4  20:52
 */
public class MotionModuleImpl implements MotionModule {
    private DcMotor[] dcMotors = new DcMotor[4]; //左前，左后，右前，右后
    private final AtomicBoolean runningMode = new AtomicBoolean(false);//是否手动操作
    private final double MAX_ANGLE_SPEED = 3600;//@warning
    private CVModule cvModule;
    public final double[] AREA_SIZE = {100, 100};
    private PIDParam rotatePIDParam = new PIDParam(0.03, 0.0001, 0, 0, -1, 1);
    private BasePID rotatePID = new PositionalPID(rotatePIDParam);

    public void setSpeed(int motor, double angleSpeed) {
        dcMotors[motor].setPower(angleSpeed / MAX_ANGLE_SPEED);
    }

    public void move(double distance, boolean isForward) {

    }

    public double turn(double angle) {
        rotatePIDParam.setTargetVal(angle);
        return rotatePID.collectAndCalculate(cvModule.getTotalAngle());
    }

    public MotionModuleImpl(HardwareMap hardwareMap) {
        dcMotors[0] = hardwareMap.dcMotor.get("l1");
        dcMotors[1] = hardwareMap.dcMotor.get("l2");
        dcMotors[2] = hardwareMap.dcMotor.get("r1");
        dcMotors[3] = hardwareMap.dcMotor.get("r2");
        //this.cvModule = cvModule;

    }

    public void moveGamepad(double move, double turn, double fun, double k) {
        double lFront;
        double lBack;
        double rFront;
        double rBack;
        double k0 = 0.7;

        //计算每个电机
        lFront = -(move + turn + fun);
        lBack = -(move + turn - fun);
        rFront = (move - turn - fun);
        rBack = (move - turn + fun);
        //找四个值中绝对值最大的值
        double max = Math.max(Math.abs(lFront), Math.abs(lBack));
        max = Math.max(max, Math.abs(rFront));
        max = Math.max(max, Math.abs(rBack));

        if (max > 1) {
            lFront /= max;
            lBack /= max;
            rFront /= max;
            rBack /= max;
        }
        synchronized (dcMotors) {
            runningMode.set(true);
            //确保没有值越界（界为[-1,1]）
            dcMotors[0].setPower(lFront*k*k0);
            dcMotors[1].setPower(lBack*k*k0);
            dcMotors[2].setPower(rFront*k*k0);
            dcMotors[3].setPower(rBack*k*k0);
            //给每个马达设置速度
        }
    }

    public void moveGamepad(double move, double turn, double fun){
        moveGamepad(move,fun,turn,1);
    }


    public final double getDistance(double a[], double b[]) {
        double ret;
        ret = Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2);
        ret = Math.sqrt(ret);
        return ret;
    }

}
