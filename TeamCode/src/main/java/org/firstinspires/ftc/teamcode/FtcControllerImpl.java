package org.firstinspires.ftc.teamcode;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.EventListener;
import java.util.HashMap;

public class FtcControllerImpl implements FtcController{
    /**
     *
     */
    private MotionModuleImpl m;
    private ShootingModuleImpl s;
    private TransportModuleImpl t;
    private RoboticArmModuleImpl a;
    private MyGamepad gamepad1;

    FtcControllerImpl(MotionModuleImpl m, ShootingModuleImpl s, TransportModuleImpl t, RoboticArmModuleImpl a, Gamepad gamepad1) throws RobotCoreException {
        this.m = m;
        this.s = s;
        this.t = t;
        this.a = a;
        this.gamepad1 = new MyGamepad(gamepad1);
        this.gamepad1.registerListener(gamepadListener);
    }



    GamepadListener gamepadListener = new GamepadListener() {
        @Override
        public void motionChanged(MotionEvent event) {

        }

        @Override
        public void keyChanged(KeyEvent event) {
            if (event.getAction()==KeyEvent.ACTION_DOWN){
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BUTTON_A:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_X:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_R1:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_L1:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBL:

                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBR:

                        break;
                }
            }
        }
    };

}

class MyGamepad extends Gamepad{
    private GamepadListener gamepadListener;
    private HashMap<Integer,Long> lastDeBounceTime;
    private final long DEDOUNCE_TIME = 50;

    public MyGamepad(Gamepad gamepad1) throws RobotCoreException {
        this.copy(gamepad1);
        final long time = System.currentTimeMillis();
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

    @Override
    public void update(MotionEvent event) {
        super.update(event);
        gamepadListener.motionChanged(event);
    }

    @Override
    public void update(final KeyEvent event) {
        if ((System.currentTimeMillis()-lastDeBounceTime.get(event.getKeyCode()))>DEDOUNCE_TIME){
            super.update(event);
            gamepadListener.keyChanged(event);
        } else{
            lastDeBounceTime.put(event.getKeyCode(),System.currentTimeMillis());
        }
    }

    public void registerListener(GamepadListener gamepadListener){
        this.gamepadListener = gamepadListener;
    }
}

interface GamepadListener extends EventListener {
    void motionChanged(MotionEvent event);
    void keyChanged(KeyEvent event);
}
