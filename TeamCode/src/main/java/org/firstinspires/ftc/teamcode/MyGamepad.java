package org.firstinspires.ftc.teamcode;

import android.util.Pair;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

class MyGamepad{
    private GamepadListener gamepadListener;
    private Gamepad gamepad;
    private Hashtable<Code, Pair<Boolean,Long>> pressTime;
    private AtomicBoolean isListening = new AtomicBoolean(false);

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

    public void registerListener(GamepadListener gamepadListener){
        this.gamepadListener = gamepadListener;
    }

    public Thread getThread(){
        return listener;
    }

    public void setIsListening(boolean isListening){
        this.isListening.set(isListening);
    }

    public boolean getIsListening(){
        return isListening.get();
    }

    private Thread listener = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                if (isListening.get()) {
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
                    gamepadListener.pressA();
                    break;
                case B:
                    gamepadListener.pressB();
                    break;
                case X:
                    gamepadListener.pressX();
                    break;
                case Y:
                    gamepadListener.pressY();
                    break;
                case RIGHT_BUMPER:
                    gamepadListener.pressR1();
                    break;
                case LEFT_BUMPER:
                    gamepadListener.pressL1();
                    break;
                case RIGHT_STICK_BUTTON:
                    gamepadListener.pressThumbR();
                    break;
                case LEFT_STICK_BUTTON:
                    gamepadListener.pressThumbL();
                    break;
            }
        }
    }
}

interface GamepadListener{
    void pressA();

    void pressB();

    void pressX();

    void pressY();

    void pressR1();

    void pressL1();

    void pressThumbR();

    void pressThumbL();
}