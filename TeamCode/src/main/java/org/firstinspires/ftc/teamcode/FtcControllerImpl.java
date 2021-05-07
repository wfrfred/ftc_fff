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
public class FtcControllerImpl implements FtcController,CallBack{

    private MotionModuleImpl motionModule;
    private ShootingModuleImpl shootingModule;
    private TransportModuleImpl transportModule;
    private RoboticArmModuleImpl roboticArmModule;
    private MyGamepad gamepad1;
    private ExecutorService executorService;

    private boolean isMotionModuleManual = false;

    /**
     * 用于实例化该控制器的构造方法
     * @param motionModule 运动模块
     * @see MotionModule
     * @param shootingModule 射击模块
     * @see ShootingModule
     * @param transportModule 运输模块
     * @see TransportModule
     * @param roboticArmModule 机械爪模块
     * @see RoboticArmModule
     * @param gamepad1 游戏手柄
     * @see MyGamepad
     * @see Gamepad
     */
    FtcControllerImpl(MotionModuleImpl motionModule, ShootingModuleImpl shootingModule, TransportModuleImpl transportModule, RoboticArmModuleImpl roboticArmModule, Gamepad gamepad1) {
        //this.motionModule = motionModule;
        this.shootingModule = shootingModule;
        //this.transportModule = transportModule;
        //this.roboticArmModule = roboticArmModule;
        this.gamepad1 = new MyGamepad(gamepad1);
        this.gamepad1.setCallBack(this);
        executorService = Executors.newCachedThreadPool();
    }

    public void startGamepadListening(){
        gamepad1.getThread().start();
    }

    public void startMotionModuleManualThread(){
        motionModuleManualThread.start();
    }

    public void setIsMotionModuleManual(boolean isMotionModuleManual){
        this.isMotionModuleManual = isMotionModuleManual;
    }

    public void setIsListening(boolean isListening){
        gamepad1.setIsListening(isListening);
    }

    public void pressA(){
        shootingModule.startMotor();
        //executorService.submit( new Thread( () -> shootingModule.startMotor()));
    }

    public void pressB() {
        shootingModule.stopMotor();
        //executorService.submit( new Thread( () -> shootingModule.stopMotor()));
    }

    public void pressX() {
        shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
        //executorService.submit( new Thread( () -> shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1)));
    }

    public void pressY() {
        shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
        //executorService.submit( new Thread( () -> shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1)));
    }

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

    public void pressL1() {

    }

    public void pressThumbR() {

    }

    public void pressThumbL() {

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

class MyGamepad{
    private CallBack callBack;
    private Gamepad gamepad;
    private Hashtable<Code,Pair<Boolean,Long>> pressTime;
    private boolean isListening = false;

    public enum Code{
        A,
        B,
        X,
        Y,
        RIGHT_BUMPER,
        LEFT_BUMPER,
        RIGHT_STICK_BUTTON,
        LEFT_STICK_BUTTON,
        RIGHT_STICK_X,
        RIGHT_STICK_Y,
        LEFT_STICK_X,
        LEFT_STICK_Y,
        RIGHT_TRIGGER,
        LEFT_TRIGGER
    }

    MyGamepad(Gamepad gamepad){
        this.gamepad = gamepad;
        long time = System.currentTimeMillis();
        pressTime = new Hashtable<Code,Pair<Boolean,Long>>(){
            {
                put(Code.A,new Pair<>(false,time));
                put(Code.B, new Pair<>(false, time));
                put(Code.X,new Pair<>(false,time));
                put(Code.Y,new Pair<>(false,time));
                put(Code.RIGHT_BUMPER,new Pair<>(false,time));
                put(Code.LEFT_BUMPER,new Pair<>(false,time));
                put(Code.RIGHT_STICK_BUTTON,new Pair<>(false,time));
                put(Code.LEFT_STICK_BUTTON,new Pair<>(false,time));
            }
        };
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public Thread getThread(){
        return listener;
    }

    public void setIsListening(boolean isListening){
        this.isListening = isListening;
    }

    private Thread listener = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                if (isListening) {
                    if (pressTime.get(Code.A).first != gamepad.a) debounce(Code.A);
                    if (pressTime.get(Code.B).first != gamepad.b) debounce(Code.B);
                    if (pressTime.get(Code.X).first != gamepad.x) debounce(Code.X);
                    if (pressTime.get(Code.Y).first != gamepad.y) debounce(Code.Y);
                    if (pressTime.get(Code.RIGHT_BUMPER).first != gamepad.right_bumper) debounce(Code.RIGHT_BUMPER);
                    if (pressTime.get(Code.LEFT_BUMPER).first != gamepad.left_bumper) debounce(Code.LEFT_BUMPER);
                    if (pressTime.get(Code.RIGHT_STICK_BUTTON).first != gamepad.right_stick_button) debounce(Code.RIGHT_STICK_BUTTON);
                    if (pressTime.get(Code.LEFT_STICK_BUTTON).first != gamepad.left_stick_button) debounce(Code.LEFT_STICK_BUTTON);
                }
            }
        }
    });

    public float getMotion(Code code){
        switch (code){
            case RIGHT_STICK_X:
                return gamepad.right_stick_x;
            case RIGHT_STICK_Y:
                return gamepad.right_stick_y;
            case LEFT_STICK_X:
                return gamepad.left_stick_x;
            case LEFT_STICK_Y:
                return gamepad.left_stick_y;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger;
            case LEFT_TRIGGER:
                return gamepad.left_trigger;
        }
        return 0;
    }

    private long getTime(Code keyCode){
        return pressTime.get(keyCode).second;
    }

    private boolean getValue(Code keyCode){
        return pressTime.get(keyCode).first;
    }

    private void setKey(Code keyCode,Pair<Boolean,Long> newKey){
        pressTime.put(keyCode,newKey);
    }

    private void debounce(Code keyCode){
        //若上次更新时间到这次小于抖动时间，则判定为抖动，否则执行方法
        //忽略与上次时间差过短
        long time = System.currentTimeMillis();
        final long DEBOUNCE_TIME = 50;
        if((time-Math.abs(getTime(keyCode)))< DEBOUNCE_TIME) return;
        if(!getValue(keyCode)){
            setKey(keyCode,new Pair<>(true,time));
            return;
        } else{
            //抬起时触发改变函数
            setKey(keyCode,new Pair<>(false,time));
            switch (keyCode){
                case A:
                    callBack.pressA();
                    break;
                case B:
                    callBack.pressB();
                    break;
                case X:
                    callBack.pressX();
                    break;
                case Y:
                    callBack.pressY();
                    break;
                case RIGHT_BUMPER:
                    callBack.pressR1();
                    break;
                case LEFT_BUMPER:
                    callBack.pressL1();
                    break;
                case RIGHT_STICK_BUTTON:
                    callBack.pressThumbR();
                    break;
                case LEFT_STICK_BUTTON:
                    callBack.pressThumbL();
                    break;
            }
        }
    }
}

interface CallBack{
    void pressA();

    void pressB();

    void pressX();

    void pressY();

    void pressR1();

    void pressL1();

    void pressThumbR();

    void pressThumbL();
}