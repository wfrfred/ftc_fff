package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.fff.controller.AbstractFtcController;
import com.fff.controller.GamepadListener;
import com.fff.controller.MyGamepad;

/**
 * 统筹管理调用各类机器人模块
 *
 * @author wfrfred
 * @version 1.21
 * @Time 2021-05-30 13:41
 */
public class FtcControllerImpl extends AbstractFtcController {
    protected final MotionModule motionModule;
    protected final ShootingModule shootingModule;
    protected final TransportModule transportModule;
    protected final RoboticArmModule roboticArmModule;
    protected final CVModule cvModule;
    private volatile boolean isMotionModuleManual = false;
    private volatile double targetTotalAngle = 0;
    Telemetry telemetry;
    private final GamepadListener gamepadListener = new GamepadListener() {

        @Override
        public void pressA() {

            roboticArmModule.putDownArm(0);
            //sleep(1000);
            //roboticArmModule.release();


        }

        @Override
        public void pressB() {
            //roboticArmModule.grab();
            //sleep(200);
            roboticArmModule.liftArm();
        }

        @Override
        public void pressX() {
            transportModule.startTransporting(false);
        }

        @Override
        public void pressY() {
            transportModule.startTransporting(true);
        }

        @Override
        public void pressR1() {
            shootingModule.shoot();
        }

        @Override
        public void pressL1() {
            if(shootingModule.isMotorStarted()) {
                shootingModule.stopMotor();
            }else {
                shootingModule.startMotor();
            }
        }

        @Override
        public void pressThumbR() {
            transportModule.stopTransporting();
        }

        @Override
        public void pressThumbL() {

        }
    };

    FtcControllerImpl(Telemetry telemetry, MotionModule motionModule, ShootingModule shootingModule, TransportModule transportModule, RoboticArmModule roboticArmModule, CVModule cvModule, Gamepad gamepad1) {
        super(gamepad1);
        this.motionModule = motionModule;
        this.shootingModule = shootingModule;
        this.transportModule = transportModule;
        this.roboticArmModule = roboticArmModule;
        this.cvModule = cvModule;
        super.setGamepadListener(gamepadListener);
        this.telemetry = telemetry;
        startMotionModuleManualThread();
        setIsMotionModuleManual(true);
    }

    public void startMotionModuleManualThread() {
        motionModuleManualThread.start();
    }

    public void setIsMotionModuleManual(boolean isMotionModuleManual) {
        this.isMotionModuleManual = isMotionModuleManual;
    }

    Thread motionModuleManualThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (isMotionModuleManual) {
                    //targetTotalAngle += gamepad1.getMotion(MyGamepad.Code.RIGHT_STICK_X);
                    synchronized (motionModule) {
                        motionModule.moveGamepad(
                                gamepad1.getMotion(MyGamepad.Code.LEFT_STICK_Y),
                                //motionModule.turn(targetTotalAngle),
                                -gamepad1.getMotion(MyGamepad.Code.RIGHT_STICK_X),
                                -gamepad1.getMotion(MyGamepad.Code.LEFT_STICK_X),
                                1 - gamepad1.getMotion(MyGamepad.Code.RIGHT_TRIGGER)
                        );
                    }
                }
            }
        }
    }, "MotionModule");

    private final void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}

