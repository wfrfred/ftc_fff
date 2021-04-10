package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ShootingModuleImpl implements ShootingModule{
    private DcMotor shootMotor;
    private Servo pushServo;
    private long startTime = 0;
    private final float SHOOT_POSITION = 0.9f;
    private int bulletAmount = 0;

    ShootingModuleImpl(HardwareMap hardwareMap){
        shootMotor = hardwareMap.dcMotor.get("shootMotor");
        pushServo = hardwareMap.servo.get("pushServo");
    }

    public void shoot(){
        if(startTime !=0&&System.currentTimeMillis()-startTime>1000){
            while(bulletAmount !=0){
                push(SHOOT_POSITION);
                resetServo();
            }
        }else{

        }
    }

    public void startMotor(){
        shootMotor.setPower(1f);
        startTime = System.currentTimeMillis();
    }

    public void stopMotor() {
        shootMotor.setPower(0f);
        startTime = 0;
    }

    public void aim(){
        //等CVModule
    }

    private void push(double position){
        pushServo.setPosition(position);
        --bulletAmount;
        while(pushServo.getPosition()==position) {
            return;
        }
    }

    private void resetServo(){
        pushServo.setPosition(0f);
        while(pushServo.getPosition()==0f) {
            return;
        }
    }

    public int getBulletAmount() {
        return bulletAmount;
    }

    public void setBulletAmount(int bulletAmount) {
        if(bulletAmount>=0&&bulletAmount<=3) {
            this.bulletAmount = bulletAmount;
        }
    }
}
