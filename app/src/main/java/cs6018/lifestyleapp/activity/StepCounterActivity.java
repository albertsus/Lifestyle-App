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
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.StepsData;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.DateUtils;

import android.widget.Button;
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

public class StepCounterActivity extends Activity implements View.OnClickListener, SensorEventListener {

    final static int DEFAULT_GOAL = 10000;
    final static float DEFAULT_STEP_SIZE = 2.5f;
    final static String DEFAULT_STEP_UNIT = "ft";
    final static int FEET_TO_DISTANCE = 5280;

    private TextView stepsView, totalView, averageView;
    private PieModel sliceGoal, sliceCurrent;
    private PieChart pg;
    private BarChart barChart;

    private int todayOffset, total_start = 100, goal, since_boot, total_days = 7;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps = true;

    private int stepCnt = 0;

    private int stepCntTest;

    List<Pair<Long, Integer>> listPair = new ArrayList<>(
            Collections.nCopies(7, new Pair<Long, Integer>(new Long(0), 0)));

    // private ImageButton mBtnStart, mBtnStop, mBtnReset;
    private Button mFlagBtn;

    GestureDetectorCompat mDetector;

    private MediaPlayer mediaPlayerOneClick, mediaPlayerDoubleClick;

    private boolean btnFlag = true;

    final Handler handler = new Handler();
    Runnable runnable;

    private static int steps;

//    List<Pair<Long, Integer>> listPair = new ArrayList<>();

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

        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());

        Long today = DateUtils.getToday();
        Log.d("Today", today.toString());
        Log.d("Today Date", df.format(new Date(today)));
        Log.d("Yesterday Date", String.valueOf(DateUtils.getNday(-1)));
        Log.d("2 days ago date", String.valueOf(DateUtils.getNday(-2)));
        Log.d("3 days ago date", String.valueOf(DateUtils.getNday(-3)));
        Log.d("4 days ago date", String.valueOf(DateUtils.getNday(-4)));
        Log.d("5 days ago date", String.valueOf(DateUtils.getNday(-5)));
        Log.d("6 days ago date", String.valueOf(DateUtils.getNday(-6)));
        Log.d("7 days ago date", String.valueOf(DateUtils.getNday(-7)));
        Log.d("8 days ago date", String.valueOf(DateUtils.getNday(-8)));

        Long tomorrow = DateUtils.getTomorrow();
        Log.d("Tomorrow", tomorrow.toString());
        Log.d("Tomorrow Date", df.format(new Date(tomorrow)));

//        int idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(0))));
//        Log.d("0", String.valueOf(idx));
//        Log.d("0", df.format(new Date(DateUtils.getNday(0))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(0), 5500));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-1))));
//        Log.d("-1", String.valueOf(idx));
//        Log.d("-1", df.format(new Date(DateUtils.getNday(-1))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-1), 4900));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-2))));
//        Log.d("-2", String.valueOf(idx));
//        Log.d("-2", df.format(new Date(DateUtils.getNday(-2))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-2), 4500));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-3))));
//        Log.d("-3", String.valueOf(idx));
//        Log.d("-3", df.format(new Date(DateUtils.getNday(-3))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-3), 7400));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-4))));
//        Log.d("-4", String.valueOf(idx));
//        Log.d("-4", df.format(new Date(DateUtils.getNday(-4))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-4), 5200));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-5))));
//        Log.d("-5", String.valueOf(idx));
//        Log.d("-5", df.format(new Date(DateUtils.getNday(-5))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-5), 1900));
//
//        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-6))));
//        Log.d("-6", String.valueOf(idx));
//        Log.d("-6", df.format(new Date(DateUtils.getNday(-6))));
//        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-6), 10500));

        stepsView = (TextView) findViewById(R.id.steps);
        totalView = (TextView) findViewById(R.id.total);
        averageView = (TextView) findViewById(R.id.average);

        // mFlagBtn = (Button) findViewById(R.id.btn_flag);
        // mFlagBtn.setOnClickListener(this);

        pg = (PieChart) findViewById(R.id.graph);
        barChart = (BarChart) findViewById(R.id.bargraph);

        // slice for the steps taken today
        sliceCurrent = new PieModel("", 0, Color.parseColor("#566655"));
        pg.addPieSlice(sliceCurrent);

        // slice for the "missing" steps until reaching the goal
        sliceGoal = new PieModel("", DEFAULT_GOAL, Color.parseColor("#a00835"));
        pg.addPieSlice(sliceGoal);


//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                updatePieTest();
//                handler.postDelayed(this, 1000);
//            }
//        };

        if(btnFlag) {
            handler.postDelayed(runnable, 1000);
            btnFlag = false;
        }

        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showSteps = !showSteps;
                stepsDistanceChanged();
            }
        });

        pg.setDrawValueInPie(false);
        pg.setUsePieRotation(true);
        pg.startAnimation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.btn_flag: {
//                if (btnFlag) {
//                    handler.postDelayed(runnable, 1000);
//                    mFlagBtn.setText("Pause");
//                } else {
//                    handler.removeCallbacks(runnable);
//                    mFlagBtn.setText("Resume");
//                }
//                btnFlag = !btnFlag;
//                break;
//            }
        }
    }

    /**
     * Call this method if the Fragment should update the "steps"/"km" text in
     * the pie graph as well as the pie and the bars graphs.
     */
    private void stepsDistanceChanged() {
        if (showSteps) {
            ((TextView) findViewById(R.id.unit)).setText(getString(R.string.steps));
        } else {
            ((TextView) findViewById(R.id.unit)).setText(DEFAULT_STEP_UNIT);
        }

        updatePieTest();
        updateBars();
    }

    private void updatePieTest() {
        // stepCnt += 10;

        if (showSteps) {
            stepsView.setText(formatter.format(stepCntTest));
            totalView.setText(formatter.format(total_start + stepCntTest));
            averageView.setText(formatter.format((total_start + stepCntTest) / total_days));
        } else {
            // update only every 10 steps when displaying distance
            float stepsize = DEFAULT_STEP_SIZE;
            float distance_today = stepCntTest * stepsize;
            float distance_total = (total_start + stepCntTest) * stepsize;
            distance_today /= 5280;
            distance_total /= 5280;
            stepsView.setText(formatter.format(distance_today));
            totalView.setText(formatter.format(distance_total));
            averageView.setText(formatter.format(distance_total / total_days));
        }

        sliceCurrent.setValue(stepCntTest);
        if (DEFAULT_GOAL - stepCntTest > 0) {
            // goal not reached yet
            if (pg.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pg.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(DEFAULT_GOAL - stepCntTest);
        } else {
            // goal reached
            pg.clearChart();
            pg.addPieSlice(sliceCurrent);
        }
        pg.update();
    }

    private void updatePie() {
        Log.d("UI - update steps: ", "" + since_boot);
        // todayOffset might still be Integer.MIN_VALUE on first start
        int steps_today = Math.max(todayOffset + since_boot, 0);
        sliceCurrent.setValue(steps_today);
        if (goal - steps_today > 0) {
            // goal not reached yet
            if (pg.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pg.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal - steps_today);
        } else {
            // goal reached
            pg.clearChart();
            pg.addPieSlice(sliceCurrent);
        }
        pg.update();
        if (showSteps) {
            stepsView.setText(formatter.format(steps_today));
            totalView.setText(formatter.format(total_start + steps_today));
            averageView.setText(formatter.format((total_start + steps_today) / total_days));
        } else {
            // update only every 10 steps when displaying distance
            float stepsize = DEFAULT_STEP_SIZE;
            float distance_today = steps_today * stepsize;
            float distance_total = (total_start + steps_today) * stepsize;
            distance_today /= FEET_TO_DISTANCE;
            distance_total /= FEET_TO_DISTANCE;
            stepsView.setText(formatter.format(distance_today));
            totalView.setText(formatter.format(distance_total));
            averageView.setText(formatter.format(distance_total / total_days));
        }
    }

    private void updateBars() {

        Query last7daysData = mDbSteps.orderByKey().limitToLast(7);
        last7daysData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
                if (barChart.getData().size() > 0) barChart.clearChart();
                barChart.setShowDecimal(!showSteps); // show decimal in distance view only

                for (DataSnapshot dayData : dataSnapshot.getChildren()) {
                    if (dayData != null) {
                        Log.d("dayData", dayData.getKey());
                        Log.d("dayData.child(steps)", dayData.child("steps").getValue(Integer.class).toString());
                        int idx = DateUtils.indexOfNday(df.format(Long.parseLong(dayData.getKey())));
                        listPair.set(idx, new Pair<Long, Integer>(Long.parseLong(dayData.getKey()), dayData.child("steps").getValue(Integer.class)));
                        // listPair.add(new Pair<Long, Integer>(Long.parseLong(dayData.getKey()), dayData.child("steps").getValue(Integer.class)));
                    } else {
                        Log.d("last7daysData:onDataChange", "null");
                    }
                }

                BarModel bm;
                float distance, stepsize = DEFAULT_STEP_SIZE;
                int steps;
                Log.v("listParisize", String.valueOf(listPair.size()));
                for (int i = 0; i < listPair.size(); i++) {
                    Pair<Long, Integer> current = listPair.get(i);
                    steps = current.second;
                    Log.d("BarChart:steps", ""+steps);
                    if (steps > 0) {

                        bm = new BarModel(df.format(new Date(current.first)), 0,
                                steps > DEFAULT_GOAL ? Color.parseColor("#566655") : Color.parseColor("#a00835"));
                        if (showSteps) {
                            Log.d("bmSetValue", "showSteps==true");
                            bm.setValue(steps);
                        } else {
                            Log.d("bmSetValue", "showSteps==false");
                            distance = steps * stepsize;
                            distance /= FEET_TO_DISTANCE;
                            distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                            bm.setValue(distance);
                        }
                        barChart.addBar(bm);
                    }
                }
                if (barChart.getData().size() > 0) {
                    Log.d("updateBars", "barChart.size()>0");
                    barChart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            // Dialog_Statistics.getDialog(this, since_boot).show();
                        }
                    });
                    barChart.startAnimation();
                } else {
                    Log.d("updateBars", "setVisibility(View.GONE)");
                    barChart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }

        stepsDistanceChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        try {
            SensorManager sm =
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

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
                        stepCntTest = 0;
                        mDbTodaySteps.setValue(stepsData);
                    } else {
                        Log.d("onSensorChanged", "today's steps already exist");
                        StepsData stepsData = dataSnapshot.getValue(StepsData.class);
                        int sensorStepsFromDB = stepsData.getLastSaveSteps();
                        int todayStepsFromDB = stepsData.getSteps();

                        if (sensorStepsFromDB != steps) {
                            stepsData.setSteps(todayStepsFromDB + 1);
                            stepCntTest = todayStepsFromDB + 1;
                            // stepsView.setText(formatter.format(stepCntTest));
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

        updatePieTest();
        // stepsDistanceChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDoubleTapEvent(MotionEvent event){
            // handler.postDelayed(runnable, 1000);
            Log.d(DEBUG_TAG,"onDoubleTap: " + event.toString());
            mediaPlayerDoubleClick.start();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            // handler.removeCallbacks(runnable);
            Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
            mediaPlayerOneClick.start();
            return true;
        }
    }

}


