package org.firstinspires.ftc.teamcode;

import android.util.Pair;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-05-07 22:05
 * @version 1.2
 */
public class FtcControllerImpl extends AbstractFtcController{

    private boolean isMotionModuleManual = false;
    private final GamepadListener gamepadListener = new GamepadListener() {

        @Override
        public void pressA(){
            shootingModule.startMotor();
            //executorService.submit( new Thread( () -> shootingModule.startMotor()));
        }

        @Override
        public void pressB() {
            shootingModule.stopMotor();
            //executorService.submit( new Thread( () -> shootingModule.stopMotor()));
        }

        @Override
        public void pressX() {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
            //executorService.submit( new Thread( () -> shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1)));
        }

        @Override
        public void pressY() {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
            //executorService.submit( new Thread( () -> shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1)));
        }

        @Override
        public void pressR1() {
        /*
        executorService.submit( new Thread( () -> {
            try {
                motionModuleManualThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shootingModule.shoot();
            motionModuleManualThread.notify();
        } ));
        */
            shootingModule.shoot();
        }

        @Override
        public void pressL1() {

        }

        @Override
        public void pressThumbR() {

        }

        @Override
        public void pressThumbL() {

        }
    };

    FtcControllerImpl(MotionModule motionModule, ShootingModule shootingModule, TransportModule transportModule, RoboticArmModule roboticArmModule, Gamepad gamepad1){
        super(motionModule,shootingModule,transportModule,roboticArmModule,gamepad1);
        super.setGamepadListener(gamepadListener);
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
                                -gamepad1.getMotion(MyGamepad.Code.RIGHT_STICK_Y),
                                1 - gamepad1.getMotion(MyGamepad.Code.RIGHT_TRIGGER)
                        );
                    }
                }
            }
    },"MotionModule");
}

