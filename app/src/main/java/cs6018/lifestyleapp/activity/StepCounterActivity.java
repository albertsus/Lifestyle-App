package cs6018.lifestyleapp.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.utils.DateUtils;

import android.widget.ImageButton;
import android.widget.TextView;

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

public class StepCounterActivity extends AppCompatActivity implements View.OnClickListener{

    final static int DEFAULT_GOAL = 10000;
    final static float DEFAULT_STEP_SIZE = 2.5f;
    final static String DEFAULT_STEP_UNIT = "ft";
    final static int FEET_TO_DISTANCE = 5280;

    private TextView stepsView, totalView, averageView;
    private PieModel sliceGoal, sliceCurrent;
    private PieChart pg;

    private int todayOffset, total_start = 100, goal, since_boot, total_days = 7;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps = true;

    private int stepCnt = 0;

    List<Pair<Long, Integer>> listPair = new ArrayList<>(
            Collections.nCopies(7, new Pair<Long, Integer>(new Long(0), 0)));

    private ImageButton mBtnStart, mBtnStop, mBtnReset;

    MediaPlayer mediaPlayer = new MediaPlayer();

    final Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_file_1);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(this);

        mBtnStop = findViewById(R.id.btn_stop);
        mBtnStop.setOnClickListener(this);

        mBtnReset = findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(this);

        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());

        Long today = DateUtils.getToday();
        Log.d("Today", today.toString());
        Log.d("Today Date", df.format(new Date(today)));

        Long tomorrow = DateUtils.getTomorrow();
        Log.d("Tomorrow", tomorrow.toString());
        Log.d("Tomorrow Date", df.format(new Date(tomorrow)));

        int idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(0))));
        Log.d("0", String.valueOf(idx));
        Log.d("0", df.format(new Date(DateUtils.getNday(0))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(0), 5500));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-1))));
        Log.d("-1", String.valueOf(idx));
        Log.d("-1", df.format(new Date(DateUtils.getNday(-1))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-1), 4900));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-2))));
        Log.d("-2", String.valueOf(idx));
        Log.d("-2", df.format(new Date(DateUtils.getNday(-2))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-2), 4500));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-3))));
        Log.d("-3", String.valueOf(idx));
        Log.d("-3", df.format(new Date(DateUtils.getNday(-3))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-3), 7400));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-4))));
        Log.d("-4", String.valueOf(idx));
        Log.d("-4", df.format(new Date(DateUtils.getNday(-4))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-4), 5200));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-5))));
        Log.d("-5", String.valueOf(idx));
        Log.d("-5", df.format(new Date(DateUtils.getNday(-5))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-5), 1900));

        idx = DateUtils.indexOfNday(df.format(new Date(DateUtils.getNday(-6))));
        Log.d("-6", String.valueOf(idx));
        Log.d("-6", df.format(new Date(DateUtils.getNday(-6))));
        listPair.set(idx, new Pair<Long, Integer>(DateUtils.getNday(-6), 10500));

        stepsView = (TextView) findViewById(R.id.steps);
        totalView = (TextView) findViewById(R.id.total);
        averageView = (TextView) findViewById(R.id.average);

        pg = (PieChart) findViewById(R.id.graph);

        // slice for the steps taken today
        sliceCurrent = new PieModel("", 0, Color.parseColor("#566655"));
        pg.addPieSlice(sliceCurrent);

        // slice for the "missing" steps until reaching the goal
        sliceGoal = new PieModel("", DEFAULT_GOAL, Color.parseColor("#a00835"));
        pg.addPieSlice(sliceGoal);

//        handler.postDelayed(new Runnable() {
//            public void run() {
//                updatePieTest();
//                handler.postDelayed(this, 1000);
//            }
//        }, 1000);

        runnable = new Runnable() {
            @Override
            public void run() {
                updatePieTest();
                handler.postDelayed(this, 1000);
            }
        };

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

            case R.id.btn_start: {
                handler.postDelayed(runnable, 1000);
                break;
            }

            case R.id.btn_stop: {
                handler.removeCallbacks(runnable);
                break;
            }

            case R.id.btn_reset: {
                stepCnt = 0;
                handler.postDelayed(runnable, 1000);
                break;
            }

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

        // updatePieTest();
        updateBars();
    }

    private void updatePieTest() {
        stepCnt += 10;
        sliceCurrent.setValue(stepCnt);
        if (DEFAULT_GOAL - stepCnt > 0) {
            // goal not reached yet
            if (pg.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pg.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(DEFAULT_GOAL - stepCnt);
        } else {
            // goal reached
            pg.clearChart();
            pg.addPieSlice(sliceCurrent);
        }
        pg.update();

        if (showSteps) {
            stepsView.setText(formatter.format(stepCnt));
            totalView.setText(formatter.format(total_start + stepCnt));
            averageView.setText(formatter.format((total_start + stepCnt) / total_days));
        } else {
            // update only every 10 steps when displaying distance
            float stepsize = DEFAULT_STEP_SIZE;
            float distance_today = stepCnt * stepsize;
            float distance_total = (total_start + stepCnt) * stepsize;
            distance_today /= 5280;
            distance_total /= 5280;
            stepsView.setText(formatter.format(distance_today));
            totalView.setText(formatter.format(distance_total));
            averageView.setText(formatter.format(distance_total / total_days));
        }
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
        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
        BarChart barChart = (BarChart) findViewById(R.id.bargraph);
        if (barChart.getData().size() > 0) barChart.clearChart();
        int steps;
        float distance, stepsize = DEFAULT_STEP_SIZE;
        boolean stepsize_cm = false;
        if (!showSteps) {
            // load some more settings if distance is needed
            stepsize = DEFAULT_STEP_SIZE;
        }
        barChart.setShowDecimal(!showSteps); // show decimal in distance view only
        BarModel bm;
        Log.v("listParisize", String.valueOf(listPair.size()));
        for (int i = 0; i < listPair.size(); i++) {
            Pair<Long, Integer> current = listPair.get(i);
            steps = current.second;
            if (steps > 0) {

                bm = new BarModel(df.format(new Date(current.first)), 0,
                        steps > DEFAULT_GOAL ? Color.parseColor("#566655") : Color.parseColor("#a00835"));
                if (showSteps) {
                    bm.setValue(steps);
                } else {
                    distance = steps * stepsize;
                    distance /= FEET_TO_DISTANCE;
                    distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                    bm.setValue(distance);
                }
                barChart.addBar(bm);
            }
        }
        if (barChart.getData().size() > 0) {
            barChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // Dialog_Statistics.getDialog(this, since_boot).show();
                }
            });
            barChart.startAnimation();
        } else {
            barChart.setVisibility(View.GONE);
        }
    }

}


