package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wfrfred
 * @Time 2021-05-08 23:20
 * @version 1.2
 */
public class RoboticArmModuleImpl implements RoboticArmModule {

  private final Servo armServo;
  private final Servo handServo;
  private final AtomicBoolean armStatus = new AtomicBoolean(ARM_UP);
  private final AtomicBoolean isGrabbed = new AtomicBoolean(false);

  RoboticArmModuleImpl(HardwareMap hardwareMap) {
    handServo = hardwareMap.servo.get("handServo");
    armServo = hardwareMap.servo.get("armServo");
  }

  public void goToTarget(MotionModule motionModule) {

  }

  public void liftArm() {
    if(armStatus.compareAndSet(ARM_DOWN,ARM_UP)){
      synchronized (armServo){
        armServo.setPosition(90);
      }
    }
  }

  public void putDownArm() {
    if(armStatus.compareAndSet(ARM_UP,ARM_DOWN)){
      synchronized (armServo){
        armServo.setPosition(0);
      }
    }
  }

  public void grab() {
    if(armStatus.get()==ARM_DOWN&&isGrabbed.compareAndSet(false,true)){
      synchronized (handServo){
        handServo.setPosition(0);
      }
    }
  }

  public void release(){
    if(armStatus.get()==ARM_DOWN&&isGrabbed.compareAndSet(true,false)){
      synchronized (handServo){
        handServo.setPosition(90);
      }
    }
  }

  public boolean isGrabbed() {
    return isGrabbed.get();
  }

  public boolean getArmStatus() {
    return armStatus.get();
  }
}

