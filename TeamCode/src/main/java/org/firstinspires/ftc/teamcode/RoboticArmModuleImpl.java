package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class RoboticArmModuleImpl implements RoboticArmModule{
  
  private long degree=0;
  private Servo armServo;
  private Servo handServo;
  private boolean signal=False;

  public void init() {
        handServo = hardwareMap.servo.get("handServo");
        armServo = hardwareMap.servo.get("armServo");
  } 
  
  
  public void getdegree(){
    return degree;    
  }
  
  
  public void putdownarm(){
    if (this.degree==20){
      armServo.setPosition(90);
      signal=True;
    }
  }  
  //@param 当手臂向下时，signal标为True，手掌打开；
  //当手臂向上举时，signal变为False，手掌握紧抓住摇摆物
  
  
  public void putuparm(){
    if(armServo.getPosition==90){
      armServo.setPosition(0);
      siganl=False;
    }
  }
  
  
  public void grab(){
    if(siganl)
      handServo.setPosition(90);
    else if
      handServo.setPosition(0);      
  }
  //@see True时，打开手掌；Flase时，握紧手掌(参数可能不太对。)
  
  
}

