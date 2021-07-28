package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.atomic.AtomicBoolean;
import com.fff.util.pid.BasePID;
import com.fff.util.pid.PIDParam;
import com.fff.util.pid.PositionalPID;

/**
 * @author wfrfred
 * @version 1.2
 * @Time 2021-05-08 23:20
 */
public class RoboticArmModuleImpl implements RoboticArmModule {

    private final DcMotor armMotor;
    private final Servo handServo;
    private final AtomicBoolean armStatus = new AtomicBoolean(ARM_UP);
    private final AtomicBoolean isGrabbed = new AtomicBoolean(false);
    private final int DOWN;
    private final int UP;
    private final Gamepad gamepad;
    private final PIDParam pidParam = new PIDParam(0.04, 0.0004, 0, 0, -0.3, 0.3);
    private final BasePID pid = new PositionalPID(pidParam);

    RoboticArmModuleImpl(HardwareMap hardwareMap, Gamepad gamepad) {
        handServo = hardwareMap.servo.get("handServo");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        int tmp = armMotor.getCurrentPosition();
        UP = tmp - 45;
        DOWN = tmp - 115;
        System.out.println(UP + "" + DOWN);
        this.gamepad = gamepad;
        armMotorThread.start();
    }

    public void liftArm() {
        armStatus.compareAndSet(ARM_DOWN, ARM_UP);
    }

    public void putDownArm(double angle) {
        armStatus.compareAndSet(ARM_UP, ARM_DOWN);
    }

    public void grab() {
        //if (armStatus.get() == ARM_DOWN && isGrabbed.compareAndSet(false, true)) {
        //    synchronized (handServo) {
                handServo.setPosition(0);
        //    }
        //}
    }

    public void release() {
        //if (armStatus.get() == ARM_DOWN && isGrabbed.compareAndSet(true, false)) {
        //    synchronized (handServo) {
                handServo.setPosition(1);
        //    }
        //}
    }

    public boolean isGrabbed() {
        return isGrabbed.get();
    }

    public boolean getArmStatus() {
        return armStatus.get();
    }

    /*
    private final void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

     */

    Thread armMotorThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (armStatus.get()) {
                    pidParam.setTargetVal(UP);
                    System.out.println(UP);
                } else {
                    pidParam.setTargetVal(DOWN);
                    System.out.println(DOWN);
                }
                double x = pid.collectAndCalculate(armMotor.getCurrentPosition());
                armMotor.setPower(x);
                System.out.println(x);
                if (gamepad.left_trigger>0.5) {
                    release();
                }else grab();
            }
        }
    });
}

