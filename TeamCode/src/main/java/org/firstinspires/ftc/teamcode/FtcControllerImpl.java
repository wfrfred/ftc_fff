package org.firstinspires.ftc.teamcode;

import android.util.Pair;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-04-05 12:38
 * @version 1.1
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
     * @throws RobotCoreException 复制手柄时可能出现无法绑定
     */
    FtcControllerImpl(MotionModuleImpl motionModule, ShootingModuleImpl shootingModule, TransportModuleImpl transportModule, RoboticArmModuleImpl roboticArmModule, Gamepad gamepad1) throws RobotCoreException {
        this.motionModule = motionModule;
        this.shootingModule = shootingModule;
        this.transportModule = transportModule;
        this.roboticArmModule = roboticArmModule;
        this.gamepad1 = new MyGamepad(gamepad1);
        this.gamepad1.setCallBack(this);
        this.gamepad1.getThread().start();

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

    public void pressA(){
        executorService.submit( new Thread( () -> {
            shootingModule.startMotor();
        } ));
    }

    public void pressB() {
        executorService.submit( new Thread( () -> {
            shootingModule.stopMotor();
        } ));
    }

    public void pressX() {
        executorService.submit( new Thread( () -> {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
        } ));
    }

    public void pressY() {
        executorService.submit( new Thread( () -> {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
        } ));
    }

    public void pressR1() {
        executorService.submit( new Thread( () -> {
            try {
                motionModuleManualThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shootingModule.shoot();
            motionModuleManualThread.notify();
        } ));
    }

    public void pressL1() {

    }

    public void pressThumbR() {

    }

    public void pressThumbL() {

    }

    Thread motionModuleManualThread = new Thread( (Runnable) () ->{
            while(true){
                if(isMotionModuleManual) {
                    synchronized (motionModule) {
                        motionModule.moveGamepad(
                                gamepad1.getMotion(gamepad1.LEFT_STICK_Y),
                                -gamepad1.getMotion(gamepad1.RIGHT_STICK_X),
                                -gamepad1.getMotion(gamepad1.RIGHT_STICK_Y),
                                1 - gamepad1.getMotion(gamepad1.RIGHT_TRIGGER)
                        );
                    }
                }
            }
    },"MotionModule");

}

class MyGamepad{
    private CallBack callBack;
    private Gamepad gamepad;
    private HashMap<Integer,Pair<Boolean,Long>> key;
    private final long DEBOUNCE_TIME = 50;
    private boolean isListening = false;

    public final int A = 0;
    public final int B = 1;
    public final int X = 2;
    public final int Y = 3;
    public final int RIGHT_BUMPER = 4;
    public final int LEFT_BUMPER = 5;
    public final int RIGHT_STICK_BUTTON = 6;
    public final int LEFT_STICK_BUTTON = 7;
    public final int RIGHT_STICK_X = 8;
    public final int RIGHT_STICK_Y = 9;
    public final int LEFT_STICK_X = 10;
    public final int LEFT_STICK_Y = 11;
    public final int RIGHT_TRIGGER = 12;
    public final int LEFT_TRIGGER = 13;

    MyGamepad(final Gamepad gamepad){
        this.gamepad = gamepad;
        final long time = System.currentTimeMillis();
        key = new HashMap<Integer,Pair<Boolean,Long>>(){
            {
                put(A,new Pair<Boolean, Long>(false,time));
                put(B,new Pair<Boolean, Long>(false,time));
                put(X,new Pair<Boolean, Long>(false,time));
                put(Y,new Pair<Boolean, Long>(false,time));
                put(RIGHT_BUMPER,new Pair<Boolean, Long>(false,time));
                put(LEFT_BUMPER,new Pair<Boolean, Long>(false,time));
                put(RIGHT_STICK_BUTTON,new Pair<Boolean, Long>(false,time));
                put(LEFT_STICK_BUTTON,new Pair<Boolean, Long>(false,time));
            }
        };
        listener.start();
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
                    if (key.get(A).first != gamepad.a) debounce(A);
                    if (key.get(B).first != gamepad.b) debounce(B);
                    if (key.get(X).first != gamepad.x) debounce(X);
                    if (key.get(Y).first != gamepad.y) debounce(Y);
                    if (key.get(RIGHT_BUMPER).first != gamepad.right_bumper) debounce(RIGHT_BUMPER);
                    if (key.get(LEFT_BUMPER).first != gamepad.left_bumper) debounce(LEFT_BUMPER);
                    if (key.get(RIGHT_STICK_BUTTON).first != gamepad.right_stick_button) debounce(RIGHT_STICK_BUTTON);
                    if (key.get(LEFT_STICK_BUTTON).first != gamepad.left_stick_button) debounce(LEFT_STICK_BUTTON);
                }
            }
        }
    },"Listener");

    public float getMotion(int code){
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

    private final long getTime(int keyCode){
        return key.get(keyCode).second;
    }

    private final boolean getValue(int keyCode){
        return key.get(keyCode).first;
    }

    private final void setKey(int keyCode,Pair newKey){
        key.put(keyCode,newKey);
    }

    private final void debounce(int keyCode){
        //若上次更新时间到这次小于抖动时间，则判定为抖动，否则执行方法
        //忽略与上次时间差过短
        long time = System.currentTimeMillis();
        if((time-Math.abs(getTime(keyCode)))<DEBOUNCE_TIME) return;
        if(!getValue(keyCode)){
            setKey(keyCode,new Pair(true,time));
            return;
        } else{
            //抬起时触发改变函数
            setKey(keyCode,new Pair(false,time));
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