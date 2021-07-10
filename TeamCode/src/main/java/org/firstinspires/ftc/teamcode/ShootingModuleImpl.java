package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShootingModuleImpl implements ShootingModule {
    private final DcMotor shootMotor;
    private final Servo pushServo;
    private final AtomicLong startTime = new AtomicLong(0);
    private final float SHOOT_POSITION = 0.9f;
    private final AtomicInteger bulletAmount = new AtomicInteger(3);

    ShootingModuleImpl(HardwareMap hardwareMap) {
        shootMotor = hardwareMap.dcMotor.get("shootMotor");
        pushServo = hardwareMap.servo.get("pushServo");
    }

    public void shoot() {
        if (startTime.get() != 0 && (System.currentTimeMillis() - startTime.get()) > 1000) {
            while (bulletAmount.get() > 0) {
                push(SHOOT_POSITION);
                resetServo();
            }
        } else {
            //Do nothing
        }
    }

    public void startMotor() {
        synchronized (shootMotor) {
            shootMotor.setPower(1f);
        }
        startTime.set(System.currentTimeMillis());
    }

    public void stopMotor() {
        synchronized (shootMotor) {
            shootMotor.setPower(0f);
        }
        startTime.set(0);
    }

    public void aim() {
        //等CVModule
    }

    private void push(double position) {
        synchronized (pushServo) {
            pushServo.setPosition(position);
            bulletAmount.getAndDecrement();
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {

            }
        }
    }

    private void resetServo() {
        synchronized (pushServo) {
            pushServo.setPosition(0f);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {

            }
        }
    }

    public int getBulletAmount() {
        return bulletAmount.get();
    }

    public synchronized void setBulletAmount(int bulletAmount) {
        if (bulletAmount >= 0 && bulletAmount <= 3) {
            this.bulletAmount.set(bulletAmount);
        }
    }
}
