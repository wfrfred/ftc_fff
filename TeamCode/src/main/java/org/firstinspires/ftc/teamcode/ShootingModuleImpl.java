package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShootingModuleImpl implements ShootingModule {
    private final DcMotor shootMotor;
    private final Servo pushServo;
    private final AtomicLong startTime = new AtomicLong(0);
    private final float SHOOT_POSITION = 0.3f;
    private AtomicBoolean isStarted = new AtomicBoolean(false);

    ShootingModuleImpl(HardwareMap hardwareMap) {
        shootMotor = hardwareMap.dcMotor.get("shootMotor");
        pushServo = hardwareMap.servo.get("pushServo");
        pushServo.setDirection(Servo.Direction.REVERSE);
        resetServo();
    }

    public void shoot() {
        if (startTime.get() != 0 && (System.currentTimeMillis() - startTime.get()) > 1000) {
                push(SHOOT_POSITION);
                resetServo();
                sleep(200);
            }
    }

    public void shoot3(){
        startMotor();
        sleep(3000);
        shoot();
        shoot();
        shoot();
        stopMotor();
    }

    public void startMotor() {
        if(!isStarted.getAndSet(true)) {
            synchronized (shootMotor) {
                shootMotor.setPower(1f);
            }
            startTime.set(System.currentTimeMillis());
        }
    }

    public void stopMotor() {
        if(isStarted.getAndSet(false)){
            synchronized (shootMotor) {
                shootMotor.setPower(0f);
            }
            startTime.set(0);
        }
    }

    @Override
    public boolean isMotorStarted() {
        return isStarted.get();
    }

    public void aim() {
        //等CVModule
    }

    private void push(double position) {
        synchronized (pushServo) {
            pushServo.setPosition(position);
            sleep(1000);
        }
    }

    private void resetServo() {
        synchronized (pushServo) {
            pushServo.setPosition(0.9f);
            sleep(1000);
        }
    }

    private final void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}
