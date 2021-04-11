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
        //this.motionModule = motionModule;
        this.shootingModule = shootingModule;
        //this.transportModule = transportModule;
        //this.roboticArmModule = roboticArmModule;
        this.gamepad1 = new MyGamepad(gamepad1);
        this.gamepad1.setCallBack(this);
        //motionModuleManualThread.start();

        executorService = Executors.newCachedThreadPool();

    }

    public void gamepadStartListen(){
        executorService.execute(gamepad1.getThread());
    }

    public void pressA(){
        executorService.execute( new Thread( () -> {
            shootingModule.startMotor();
        } ));
    }

    public void pressB() {
        executorService.execute( new Thread( () -> {
            shootingModule.stopMotor();
        } ));
    }

    public void pressX() {
        executorService.execute( new Thread( () -> {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
        } ));
    }

    public void pressY() {
        executorService.execute( new Thread( () -> {
            shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
        } ));
    }

    public void pressR1() {
        shootingModule.shoot();
    }

    public void pressL1() {

    }

    public void pressThumbR() {

    }

    public void pressThumbL() {

    }

    public void setMotionModuleManual(boolean isMotionModuleManual){
        this.isMotionModuleManual = isMotionModuleManual;
    }

    public boolean isMotionModuleManual(){
        return isMotionModuleManual;
    }

    Thread motionModuleManualThread = new Thread(new Runnable(){
        @Override
        public void run() {
            while(true){
                if(isMotionModuleManual) {
                    synchronized (motionModule) {
                        motionModule.moveGamepad(
                                gamepad1.getMotion(0),
                                -gamepad1.getMotion(1),
                                -gamepad1.getMotion(2),
                                1
                        );
                    }
                }else{
                    //do something
                }
            }
        }
    },"MotionModule") {
    };

}

class MyGamepad{
    CallBack callBack;
    private Gamepad gamepad;
    private HashMap<Integer,Pair<Boolean,Long>> key;
    private final long DEBOUNCE_TIME = 50;
    private boolean isListening = false;

    MyGamepad(final Gamepad gamepad){
        this.gamepad = gamepad;
        final long time = System.currentTimeMillis();
        key = new HashMap<Integer,Pair<Boolean,Long>>(){
            {
                put(0,new Pair<Boolean, Long>(false,time));
                put(1,new Pair<Boolean, Long>(false,time));
                put(2,new Pair<Boolean, Long>(false,time));
                put(3,new Pair<Boolean, Long>(false,time));
                put(4,new Pair<Boolean, Long>(false,time));
                put(5,new Pair<Boolean, Long>(false,time));
                put(6,new Pair<Boolean, Long>(false,time));
                put(7,new Pair<Boolean, Long>(false,time));
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
    Thread listener = new Thread(new Runnable() {
        @Override
        public void run() {
            if(isListening) {
                if (key.get(0).first != gamepad.a) debounce(0);
                if (key.get(1).first != gamepad.b) debounce(1);
                if (key.get(2).first != gamepad.x) debounce(2);
                if (key.get(3).first != gamepad.y) debounce(3);
                if (key.get(4).first != gamepad.right_bumper) debounce(4);
                if (key.get(5).first != gamepad.left_bumper) debounce(5);
                if (key.get(6).first != gamepad.right_stick_button) debounce(6);
                if (key.get(7).first != gamepad.left_stick_button) debounce(7);
            }
        }
    },"Listener");

    public float getMotion(int code){
        switch (code){
            case 0:
                return gamepad.right_stick_x;
            case 1:
                return gamepad.right_stick_y;
            case 2:
                return gamepad.left_stick_x;
            case 3:
                return gamepad.left_stick_y;
            case 4:
                return gamepad.right_trigger;
            case 5:
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
                case 0:
                    callBack.pressA();
                    break;
                case 1:
                    callBack.pressB();
                    break;
                case 2:
                    callBack.pressX();
                    break;
                case 3:
                    callBack.pressY();
                    break;
                case 4:
                    callBack.pressR1();
                    break;
                case 5:
                    callBack.pressL1();
                    break;
                case 6:
                    callBack.pressThumbR();
                    break;
                case 7:
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