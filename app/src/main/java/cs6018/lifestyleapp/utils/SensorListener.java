package cs6018.lifestyleapp.utils;

import android.app.Service;
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

import cs6018.lifestyleapp.general.StepsData;
import cs6018.lifestyleapp.general.User;

/**
 * Created by suchaofan on 10/28/18.
 */

public class SensorListener extends Service implements SensorEventListener {

    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;

    private static int sensorSteps;

    private DatabaseReference mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());
    private DatabaseReference mDbSteps = mDbUsers.child("Steps");
    private DatabaseReference mDbTodaySteps = mDbSteps.child("" + DateUtils.getToday());

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] > Integer.MAX_VALUE) {
            return;
        } else {
            sensorSteps = (int) sensorEvent.values[0];

            mDbTodaySteps.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) { // initialing data
                        StepsData stepsData = new StepsData(0, 0);
                        mDbTodaySteps.setValue(stepsData);
                    } else {
                        StepsData stepsData = dataSnapshot.getValue(StepsData.class);
                        int sensorStepsFromDB = stepsData.getLastSaveSteps();
                        int todayStepsFromDB = stepsData.getSteps();

                        if (sensorStepsFromDB != sensorSteps) {
                            stepsData.setSteps(todayStepsFromDB + 1);
                            stepsData.setLastSaveSteps(sensorSteps);
                            mDbTodaySteps.setValue(stepsData);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(sensor.getName(), " accuracy changed: " + i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate","SensorListener onCreate");
        reRegisterSensor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "SensorListener onDestroy");
        try {
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void reRegisterSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sensorManager.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));
    }
}
