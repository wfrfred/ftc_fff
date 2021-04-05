package org.firstinspires.ftc.teamcode;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.EventListener;
import java.util.HashMap;

/**
 * 统筹管理调用各类机器人模块
 * @author wfrfred
 * @Time 2021-04-05 1:15
 * @version 1.0
 */
public class FtcControllerImpl implements FtcController{

    private MotionModuleImpl m;
    private ShootingModuleImpl s;
    private TransportModuleImpl t;
    private RoboticArmModuleImpl a;
    private MyGamepad gamepad1;

    /**
     * 用于实例化该控制器的构造方法
     * @param m 运动模块
     * @see MotionModule
     * @param s 射击模块
     * @see ShootingModule
     * @param t 运输模块
     * @see TransportModule
     * @param a 机械爪模块
     * @see RoboticArmModule
     * @param gamepad1 游戏手柄
     * @see MyGamepad
     * @see Gamepad
     * @throws RobotCoreException 复制手柄时可能出现无法绑定
     */
    FtcControllerImpl(MotionModuleImpl m, ShootingModuleImpl s, TransportModuleImpl t, RoboticArmModuleImpl a, Gamepad gamepad1) throws RobotCoreException {
        this.m = m;
        this.s = s;
        this.t = t;
        this.a = a;
        this.gamepad1 = new MyGamepad(gamepad1);
        this.gamepad1.registerListener(gamepadListener);
    }

    public void pressA(){

    }

    public void pressB() {

    }

    public void pressX() {

    }

    public void pressY() {

    }

    public void pressR1() {

    }

    public void pressL1() {

    }

    public void pressThumbR() {

    }

    public void pressThumbL() {

    }


    /**
     * 匿名内部类 用于注册监听器
     */
    GamepadListener gamepadListener = new GamepadListener() {
        @Override
        public void motionChanged(MotionEvent event) {

        }

        @Override
        public void keyChanged(KeyEvent event) {
            if (event.getAction()==KeyEvent.ACTION_DOWN){
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
                }
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
        this.copy(gamepad1);
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
     *调用监听器方法
     */
    @Override
    public void update(MotionEvent event) {
        super.update(event);
        gamepadListener.motionChanged(event);
    }

    /**
     *调用监听器方法
     */
    @Override
    public void update(final KeyEvent event) {

        //若上次更新时间到这次小于抖动时间，则判定为抖动，否则执行方法
        long time = System.currentTimeMillis();
        //忽略与上次时间差过短
        if((time-Math.abs(getTime(event)))<DEDOUNCE_TIME) return;
        //用时间的正数表示按下状态，负数表示抬起状态，改变时重置
        if(getTime(event)<0){
            //第一次按下时取反
            setTime(event.getKeyCode(),time);
            return;
        } else{
            //抬起时触发改变函数
            setTime(event.getKeyCode(),-time);
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

    private  final void setTime(int keyCode, long value){
        lastDeBounceTime.put(keyCode,value);
    }

}

interface GamepadListener extends EventListener {
    void motionChanged(MotionEvent event);
    void keyChanged(KeyEvent event);
}
