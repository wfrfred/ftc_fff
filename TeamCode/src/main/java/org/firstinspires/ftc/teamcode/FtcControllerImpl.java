package org.firstinspires.ftc.teamcode;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.EventListener;
import java.util.HashMap;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-04-05 12:38
 * @version 1.1
 */
public class FtcControllerImpl implements FtcController{

    private MotionModuleImpl motionModule;
    private ShootingModuleImpl shootingModule;
    private TransportModuleImpl transportModule;
    private RoboticArmModuleImpl roboticArmModule;
    private MyGamepad gamepad1;

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
        this.gamepad1.registerListener(gamepadListener);
        //motionModuleManualThread.start();
    }

    public void pressA(){
        shootingModule.startMotor();
    }

    public void pressB() {
        shootingModule.stopMotor();
    }

    public void pressX() {
        shootingModule.setBulletAmount(shootingModule.getBulletAmount()+1);
    }

    public void pressY() {
        shootingModule.setBulletAmount(shootingModule.getBulletAmount()-1);
    }

    public void pressR1() {

    }

    public void pressL1() {

    }

    public void pressThumbR() {
        shootingModule.shoot();
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
                                gamepad1.left_stick_x,
                                gamepad1.right_stick_y,
                                gamepad1.left_stick_x,
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


    GamepadListener gamepadListener = new GamepadListener() {
        @Override
        public void motionChanged(MotionEvent event) {

        }

        @Override
        public void keyChanged(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BUTTON_A:
                        pressA();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        pressB();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_X:
                        pressX();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y:
                        pressY();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_R1:
                        pressR1();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_L1:
                        pressL1();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBR:
                        pressThumbR();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBL:
                        pressThumbL();
                        break;
                    default:
                        //do nothing
                        break;
                }
            }
    };

}


/**
 * 重写Gamepad类
 * @see Gamepad
 * @author wfrfred
 * @Time 2021-04-04 1:15
 */

class MyGamepad extends Gamepad{
    private GamepadListener gamepadListener;
    private HashMap<Integer,Long> lastDeBounceTime;//用于每个按钮分别除抖动
    private final long DEDOUNCE_TIME = 50;//抖动时间

    /**
     * 用其他手柄创建
     * @param gamepad1 用于绑定手柄
     * @throws RobotCoreException 绑定失败后抛出
     */
    public MyGamepad(Gamepad gamepad1) throws RobotCoreException {
        copy(gamepad1);
        final long time = -System.currentTimeMillis();
        //初始化抖动时间列表
        lastDeBounceTime= new HashMap<Integer,Long>(){
            {
                put(KeyEvent.KEYCODE_BUTTON_A,time);
                put(KeyEvent.KEYCODE_BUTTON_B, time);
                put(KeyEvent.KEYCODE_BUTTON_X, time);
                put(KeyEvent.KEYCODE_BUTTON_Y, time);
                put(KeyEvent.KEYCODE_BUTTON_R1, time);
                put(KeyEvent.KEYCODE_BUTTON_L1, time);
                put(KeyEvent.KEYCODE_BUTTON_THUMBL, time);
                put(KeyEvent.KEYCODE_BUTTON_THUMBR, time);
            }
        };
    }

    /**
     * 调用监听器方法
     */
    @Override
    public void update(MotionEvent event) {
        super.update(event);
        gamepadListener.motionChanged(event);
    }

    /**
     * 调用监听器方法
     */
    @Override
    public void update(KeyEvent event) {
        //若上次更新时间到这次小于抖动时间，则判定为抖动，否则执行方法
        long time = System.currentTimeMillis();
        //忽略与上次时间差过短
        if((time-Math.abs(getTime(event)))<DEDOUNCE_TIME) return;
        //用时间的正数表示按下状态，负数表示抬起状态，改变时重置
        if(getTime(event)<0){
            //第一次按下时改变为正值
            setTime(event,time);
            return;
        } else{
            //抬起时触发改变函数
            setTime(event,-time);
            super.update(event);
            gamepadListener.keyChanged(event);
        }
    }

    public void registerListener(GamepadListener gamepadListener){
        this.gamepadListener = gamepadListener;
    }

    private final long getTime(KeyEvent event){
        return lastDeBounceTime.get(event.getKeyCode());
    }

    private  final void setTime(KeyEvent event, long value){
        lastDeBounceTime.put(event.getKeyCode(),value);
    }

}

interface GamepadListener extends EventListener {
    void motionChanged(MotionEvent event);
    void keyChanged(KeyEvent event);
}
