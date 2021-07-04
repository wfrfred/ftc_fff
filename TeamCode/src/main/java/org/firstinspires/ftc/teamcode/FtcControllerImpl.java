package org.firstinspires.ftc.teamcode;

import android.util.Pair;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-05-30 13:41
 * @version 1.21
 */
public class FtcControllerImpl extends AbstractFtcController{

    private volatile boolean isMotionModuleManual = false;
    private final GamepadListener gamepadListener = new GamepadListener() {

        @Override
        public void pressA() {
            double[] target = cvModule.getTarget();

            if(target[0]<0|target[1]<0){
                return;
            }

            try {
                motionModuleManualThread.wait();
                motionModule.turn(target[0]);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pressB() {
            //shootingModule.stopMotor();
        }

        @Override
        public void pressX() {
            //shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
        }

        @Override
        public void pressY() {
            //shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
        }

        @Override
        public void pressR1() {
            //shootingModule.shoot();
        }

        @Override
        public void pressL1() {

        }

        @Override
        public void pressThumbR() {
            setIsMotionModuleManual(true);
        }

        @Override
        public void pressThumbL() {
            setIsMotionModuleManual(false);
        }
    };

    FtcControllerImpl(MotionModule motionModule, ShootingModule shootingModule, TransportModule transportModule, RoboticArmModule roboticArmModule,CVModule cvModule, Gamepad gamepad1){
        super(motionModule,shootingModule,transportModule,roboticArmModule,cvModule,gamepad1);
    }

    public void init(){
        super.setGamepadListener(gamepadListener);
        startMotionModuleManualThread();
    }

    public void startMotionModuleManualThread(){
        motionModuleManualThread.start();
    }

    public void setIsMotionModuleManual(boolean isMotionModuleManual){
        this.isMotionModuleManual = isMotionModuleManual;
    }

    Thread motionModuleManualThread = new Thread(() ->{
            while(true){
                if(isMotionModuleManual) {
                    synchronized (motionModule) {
                        motionModule.moveGamepad(
                                gamepad1.getMotion(MyGamepad.Code.LEFT_STICK_Y),
                                -gamepad1.getMotion(MyGamepad.Code.RIGHT_STICK_X),
                                -gamepad1.getMotion(MyGamepad.Code.LEFT_STICK_X),
                                1 - gamepad1.getMotion(MyGamepad.Code.RIGHT_TRIGGER)
                        );
                    }
                }
            }
    },"MotionModule");
}

