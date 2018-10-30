package cs6018.lifestyleapp.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.StepsData;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.DateUtils;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepCounterActivity extends Activity implements SensorEventListener {

    final static int DEFAULT_GOAL = 500;
    final static float DEFAULT_STEP_SIZE = 2.5f; // unit: ft
    final static int FEET_TO_MILE = 5280;

    private TextView stepsView, totalView, averageView;

    private PieModel pieGoal, pieCurrent;
    private PieChart pieChart;
    private BarChart barChart;

    private int totalSteps = 0, totalDays = 1;

    private boolean showSteps = true;
    private boolean startFlag = true;

    private ImageButton mIbStart;

    private int todayStepCnt;

    List<Pair<Long, Integer>> listPair = new ArrayList<>(
            Collections.nCopies(7, new Pair<Long, Integer>(new Long(0), 0)));

    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());

    GestureDetectorCompat mDetector;

    private MediaPlayer mediaPlayerOneClick, mediaPlayerDoubleClick;

    private static int sensorSteps;

    private DatabaseReference mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());
    private DatabaseReference mDbSteps = mDbUsers.child("Steps");
    private DatabaseReference mDbTodaySteps = mDbSteps.child("" + DateUtils.getToday());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        mediaPlayerOneClick = MediaPlayer.create(this, R.raw.single_click);
        mediaPlayerDoubleClick = MediaPlayer.create(this, R.raw.double_click);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        stepsView = (TextView) findViewById(R.id.steps);
        totalView = (TextView) findViewById(R.id.total);
        averageView = (TextView) findViewById(R.id.average);

        mIbStart = (ImageButton) findViewById(R.id.ib_start);

        pieChart = (PieChart) findViewById(R.id.graph);
        barChart = (BarChart) findViewById(R.id.bargraph);

        // current pie slice
        pieCurrent = new PieModel("", 0, Color.parseColor("#566655"));
        pieChart.addPieSlice(pieCurrent);

        // goal pie slice
        pieGoal = new PieModel("", DEFAULT_GOAL, Color.parseColor("#a00835"));
        pieChart.addPieSlice(pieGoal);

        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showSteps = !showSteps;
                updateStepsDistance();
            }
        });

        pieChart.setDrawValueInPie(false);
        pieChart.setUsePieRotation(true);
        pieChart.startAnimation();
    }

    private void updateStepsDistance() {
        if (showSteps) {
            ((TextView) findViewById(R.id.unit)).setText(getString(R.string.steps));
        } else {
            ((TextView) findViewById(R.id.unit)).setText("mile");
        }

        updatePieData();
        updateBarsData();
    }

    private void updatePieData() {
        pieCurrent.setValue(todayStepCnt);
        if (DEFAULT_GOAL - todayStepCnt > 0) { // not reached goal yet
            if (pieChart.getData().size() == 1) {
                pieChart.addPieSlice(pieGoal);
            }
            pieGoal.setValue(DEFAULT_GOAL - todayStepCnt);
        } else {
            pieChart.clearChart();
            pieChart.addPieSlice(pieCurrent);
        }
        pieChart.update();

        updatePieTextViewData();
    }

    private void updatePieTextViewData() {
        if (showSteps) {
            stepsView.setText(formatter.format(todayStepCnt));
            totalView.setText(formatter.format(totalSteps + todayStepCnt));
            averageView.setText(formatter.format((totalSteps + todayStepCnt) / totalDays));
        } else {
            float todayDistance = todayStepCnt * DEFAULT_STEP_SIZE;
            float totalDistance = (totalSteps + todayStepCnt) * DEFAULT_STEP_SIZE;
            todayDistance /= FEET_TO_MILE;
            totalDistance /= FEET_TO_MILE;
            stepsView.setText(formatter.format(todayDistance));
            totalView.setText(formatter.format(totalDistance));
            averageView.setText(formatter.format(totalDistance / totalDays));
        }
    }

    private void updateBarsData() {
        Query last7daysData = mDbSteps.orderByKey().limitToLast(7);
        last7daysData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (barChart.getData().size() > 0) barChart.clearChart();
                barChart.setShowDecimal(!showSteps); // show decimal in distance view only

                for (DataSnapshot dayData : dataSnapshot.getChildren()) {
                    if (dayData != null) {
                        Log.d("dayData", dayData.getKey());
                        Log.d("dayData.child(steps)", dayData.child("steps").getValue(Integer.class).toString());
                        int idx = DateUtils.indexOfNday(df.format(Long.parseLong(dayData.getKey())));
                        listPair.set(idx, new Pair<Long, Integer>(Long.parseLong(dayData.getKey()), dayData.child("steps").getValue(Integer.class)));
                    }
                }

                showBarData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showBarData() {
        BarModel barModel;
        float distance = DEFAULT_STEP_SIZE;
        int steps;
        for (int i = 0; i < listPair.size(); i++) {
            Pair<Long, Integer> current = listPair.get(i);
            steps = current.second;
            if (steps > 0) {
                barModel = new BarModel(df.format(new Date(current.first)), 0,
                        steps > DEFAULT_GOAL ? Color.parseColor("#566655") : Color.parseColor("#a00835"));
                if (showSteps) {
                    barModel.setValue(steps);
                } else {
                    distance = steps * DEFAULT_STEP_SIZE;
                    distance /= FEET_TO_MILE;
                    distance = Math.round(distance * 1000) / 1000f;
                    barModel.setValue(distance);
                }
                barChart.addBar(barModel);
            }
        }
        if (barChart.getData().size() > 0) {
            barChart.startAnimation();
        } else {
            barChart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }

        initStepsData();

        updateStepsDistance();
    }

    private void initStepsData() {
        Query last7daysData = mDbSteps.orderByKey().limitToLast(7);
        last7daysData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dayData : dataSnapshot.getChildren()) {
                    if (dayData != null) {
                        if ((long)Long.parseLong(dayData.getKey()) == (DateUtils.getToday())) {
                            todayStepCnt = dayData.child("steps").getValue(Integer.class);
                            continue;
                        }
                        totalDays++;
                        totalSteps += dayData.child("steps").getValue(Integer.class);
                    }
                }

                stepsView.setText(formatter.format(todayStepCnt));
                totalView.setText(formatter.format(totalSteps));
                averageView.setText(formatter.format(totalSteps / totalDays));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        todayStepCnt = 0;
                        mDbTodaySteps.setValue(stepsData);
                    } else {
                        if (startFlag) {
                            StepsData stepsData = dataSnapshot.getValue(StepsData.class);
                            int sensorStepsFromDB = stepsData.getLastSaveSteps();
                            int todayStepsFromDB = stepsData.getSteps();

                            if (sensorStepsFromDB != sensorSteps) {
                                stepsData.setSteps(todayStepsFromDB + 1);
                                todayStepCnt = todayStepsFromDB + 1;
                                stepsData.setLastSaveSteps(sensorSteps);
                                mDbTodaySteps.setValue(stepsData);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        updatePieData();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDoubleTapEvent(MotionEvent event){
            Log.d(DEBUG_TAG,"onDoubleTap: " + event.toString());
            mediaPlayerDoubleClick.start();
            mIbStart.setImageResource(R.drawable.ic_stop);
            startFlag = true;
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
            mediaPlayerOneClick.start();
            mIbStart.setImageResource(R.drawable.ic_start);
            startFlag = false;
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}


