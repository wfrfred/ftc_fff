package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractFtcController implements FtcController{
    protected final MotionModule motionModule ;
    protected final ShootingModule shootingModule;
    protected final TransportModule transportModule;
    protected final RoboticArmModule roboticArmModule;
    protected final MyGamepad gamepad1;
    protected final ExecutorService executorService;
    protected GamepadListener gamepadListener;

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
    AbstractFtcController(MotionModule motionModule, ShootingModule shootingModule, TransportModule transportModule, RoboticArmModule roboticArmModule, Gamepad gamepad1) {
        this.motionModule = motionModule;
        this.shootingModule = shootingModule;
        this.transportModule = transportModule;
        this.roboticArmModule = roboticArmModule;
        this.gamepad1 = new MyGamepad(gamepad1);
        executorService = Executors.newCachedThreadPool();
    }

    public void setGamepadListener(GamepadListener gamepadListener){
        this.gamepadListener = gamepadListener;
    }

    public void startGamepadListening()throws NullPointerException{
        if(gamepadListener != null) {
            gamepad1.registerListener(gamepadListener);
            gamepad1.getThread().start();
        }else {
            throw(new NullPointerException());
        }
    }

    public void setIsListening(boolean isListening){
        gamepad1.setIsListening(isListening);
    }

    public boolean getIsListening() {
        return gamepad1.getIsListening();
    }

}
