package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.hardware.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class CVModuleImpl implements CVModule, SensorEventListener {
    private final SensorManager sManager;
    private final Sensor mSensorOrientation;
    private double angle;
    private double totalAngle;
    private int turns = 0;

    CVModuleImpl() {
        sManager = (SensorManager) getCurrentActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorOrientation = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public double[] getLocation() {
        return null;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public double getTotalAngle() {
        return totalAngle;
    }

    @Override
    public double[] getTarget() {
        return null;
    }

    @Override
    public int getMode() {
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (angle > 180 && event.values[0] < 180) {
            turns++;
        }
        if (angle < 180 && event.values[0] > 180) {
            turns--;
        }
        totalAngle = turns * 360 + event.values[0];
        angle = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //获取当前的Activity
    public Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
