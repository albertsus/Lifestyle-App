package cs6018.lifestyleapp.utils;

import android.app.AlarmManager;
import android.app.Service;
import android.arch.persistence.room.Database;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import cs6018.lifestyleapp.general.StepsData;
import cs6018.lifestyleapp.general.User;

/**
 * Created by suchaofan on 10/28/18.
 */

public class SensorListener extends Service implements SensorEventListener {

    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;
//    private final static long SAVE_OFFSET_TIME = AlarmManager.INTERVAL_HOUR;
//    private final static int SAVE_OFFSET_STEPS = 500;

    private static int steps;
//    private static int lastSaveSteps;
//    private static long lastSaveTime;

    private DatabaseReference mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());
    private DatabaseReference mDbSteps = mDbUsers.child("Steps");
    private DatabaseReference mDbTodaySteps = mDbSteps.child("" + DateUtils.getToday());

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] > Integer.MAX_VALUE) {
            Log.d("onSensorChanged", "probably not a real value: " + sensorEvent.values[0]);
            return;
        } else {
            steps = (int) sensorEvent.values[0];
            Log.d("onSensorChanged", "Steps from service are: " + steps);

            mDbTodaySteps.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Log.d("onSensorChanged", "today's steps not exist");
                        StepsData stepsData = new StepsData(0, 0);
                        mDbTodaySteps.setValue(stepsData);
                    } else {
                        Log.d("onSensorChanged", "today's steps already exist");
                        StepsData stepsData = dataSnapshot.getValue(StepsData.class);
                        int sensorStepsFromDB = stepsData.getLastSaveSteps();
                        int todayStepsFromDB = stepsData.getSteps();

                        if (sensorStepsFromDB != steps) {
                            stepsData.setSteps(todayStepsFromDB + 1);
                            stepsData.setLastSaveSteps(steps);
                            mDbTodaySteps.setValue(stepsData);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // updateIfNecessary();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(sensor.getName(), " accuracy changed: " + i);
    }

//    private void updateIfNecessary() {
//        if (steps > lastSaveSteps + SAVE_OFFSET_STEPS ||
//                (steps > 0 && System.currentTimeMillis() > lastSaveTime + SAVE_OFFSET_TIME)) {
//            Log.d("updateIfNecessary", "saving steps: steps="
//                    + steps + " lastSave=" + lastSaveSteps + " lastSaveTime=" + new Date(lastSaveTime));
//            mDbTodaySteps.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    StepsData stepsData = dataSnapshot.getValue(StepsData.class);
//                    if (stepsData.getSteps() == Integer.MIN_VALUE) {
//                        int pauseDifference = steps - dataSnapshot.child("pauseCount").getValue(Integer.class);
//
//                    }
////                    if ((int) dataSnapshot.child("steps").getValue() == Integer.MIN_VALUE) {
////                        int pauseDifference = steps - (int) dataSnapshot.child("pauseCount").getValue();
////
////                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
////            if (mDbUsers(DateUtils.getToday()) == Integer.MIN_VALUE) {
////                int pauseDifference = steps -
////                        getSharedPreferences("pedometer", Context.MODE_PRIVATE)
////                                .getInt("pauseCount", steps);
////                db.insertNewDay(Util.getToday(), steps - pauseDifference);
////                if (pauseDifference > 0) {
////                    // update pauseCount for the new day
////                    getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
////                            .putInt("pauseCount", steps).commit();
////                }
////            }
////            db.saveCurrentSteps(steps);
////            db.close();
////            lastSaveSteps = steps;
////            lastSaveTime = System.currentTimeMillis();
//        }
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate","SensorListener onCreate");
        reRegisterSensor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void reRegisterSensor() {
        Log.d("reRegisterSensor", "re-register sensor listener");
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("reRegisterSensor", "step sensors: " + sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size());
        if (sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
        Log.d("reRegisterSensor", "default: " + sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).getName());

        // enable batching with delay of max 5 min
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));
    }
}
