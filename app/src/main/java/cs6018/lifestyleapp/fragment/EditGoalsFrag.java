package cs6018.lifestyleapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;

/**
 * Created by suchaofan on 9/29/18.
 */

public class EditGoalsFrag extends Fragment implements View.OnClickListener {

    private String mTargetWeight, mTargetBMI, mTargetCalories, mTargetHikes, mWeightGoal;

    private SeekBar mSbTargetWeight, mSbTargetBMI, mSbTargetCalories, mSbTargetHikes;
    private TextView mTvMinWeight, mTvMaxWeight, mTvMinBMI, mTvMaxBMI, mTvMinCalories, mTvMaxCalories, mTvMinHikes, mTvMaxHikes;
    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes;
    private Spinner spinnerWeightGoal;
    private Button mBtUpdate;
    private RadioGroup rgWeightGoal;
    private RadioButton rbWeightGoal;

    private static final int MIN_WEIGHT = 50, MAX_WEIGHT = 600;
    private static final int MIN_BMI = 5, MAX_BMI = 50;
    private static final int MIN_CALORIES = 100, MAX_CALORIES = 5000;
    private static final int MIN_HIKES = 0, MAX_HIKES = 10;

    private DatabaseReference mDbUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_edit_goals, container, false);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());

        mTvMinWeight = view.findViewById(R.id.tv_weight_start);
        mTvMaxWeight = view.findViewById(R.id.tv_weight_end);
        mTvMinBMI = view.findViewById(R.id.tv_bmi_start);
        mTvMaxBMI = view.findViewById(R.id.tv_bmi_end);
        mTvMinCalories = view.findViewById(R.id.tv_calories_start);
        mTvMaxCalories = view.findViewById(R.id.tv_calories_end);
        mTvMinHikes = view.findViewById(R.id.tv_hikes_start);
        mTvMaxHikes = view.findViewById(R.id.tv_hikes_end);

        mSbTargetWeight = view.findViewById(R.id.sb_target_weight);
        mSbTargetBMI = view.findViewById(R.id.sb_target_bmi);
        mSbTargetCalories = view.findViewById(R.id.sb_target_calories);
        mSbTargetHikes = view.findViewById(R.id.sb_target_hikes);

        mTvTargetWeight = view.findViewById(R.id.tv_weight_data);
        mTvTargetBMI = view.findViewById(R.id.tv_bmi_data);
        mTvTargetCalories = view.findViewById(R.id.tv_calories_data);
        mTvTargetHikes = view.findViewById(R.id.tv_hikes_data);

        //spinnerWeightGoal = view.findViewById(R.id.spinner_weight_goal);
        rgWeightGoal = (RadioGroup) view.findViewById(R.id.rg_weight_goal);
        rbWeightGoal = (RadioButton) view.findViewById(rgWeightGoal.getCheckedRadioButtonId());
        rgWeightGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbWeightGoal = (RadioButton) view.findViewById(checkedId);
            }
        });

        mBtUpdate = (Button) view.findViewById(R.id.bt_update_goals);
        mBtUpdate.setOnClickListener(this);

        //spinnerWeightGoal = (Spinner) view.findViewById(R.id.spinner_weight_goal);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weigth_goal_array, R.layout.spinner_layout);
        //adapter.setDropDownViewResource(R.layout.spinner_layout);
        //spinnerWeightGoal.setAdapter(adapter);

        // Init the target data received from GoalsFrag
        InitGoalsData();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_update_goals:
                // Check valid data entered
                if (!isValidData()) {
                    break;
                } else {
                    // Show updated success info
                    Toast toast = Toast.makeText(getActivity(),
                            "Updated Goals Success!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();

                    // Update User Goals
                    updateGoals();

                    // Route to ProfileFrag
                    getFragmentManager().popBackStackImmediate();
                }
        }
    }

    /**
     * Init the goals data when first in the fragment
     */
    private void InitGoalsData() {
        mTvMinWeight.setText("" + MIN_WEIGHT);
        mTvMaxWeight.setText("" + MAX_WEIGHT);
        mTvMinBMI.setText("" + MIN_BMI);
        mTvMaxBMI.setText("" + MAX_BMI);
        mTvMinCalories.setText("" + MIN_CALORIES);
        mTvMaxCalories.setText("" + MAX_CALORIES);
        mTvMinHikes.setText("" + MIN_HIKES);
        mTvMaxHikes.setText("" + MAX_HIKES);

        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Set the target data
                mTargetWeight = dataSnapshot.child("targetWeight").getValue(String.class);
                mTargetBMI = dataSnapshot.child("targetBMI").getValue(String.class);
                mTargetCalories = dataSnapshot.child("targetDailyCalories").getValue(String.class);
                mTargetHikes = dataSnapshot.child("targetHikes").getValue(String.class);

                setBar(mSbTargetWeight, mTvTargetWeight, Integer.valueOf(mTargetWeight), MIN_WEIGHT, MAX_WEIGHT);
                setBar(mSbTargetBMI, mTvTargetBMI, Integer.valueOf(mTargetBMI), MIN_BMI, MAX_BMI);
                setBar(mSbTargetCalories, mTvTargetCalories, Integer.valueOf(mTargetCalories), MIN_CALORIES, MAX_CALORIES);
                setBar(mSbTargetHikes, mTvTargetHikes, Integer.valueOf(mTargetHikes), MIN_HIKES, MAX_HIKES);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setBar(SeekBar sb, final TextView tv, int progress, int min, int max) {
        sb.setMin(min);
        sb.setMax(max);
        sb.setProgress(progress);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                tv.setText("" + progress);
                tv.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2 - 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Update the goals once click 'Edit' Button
     */
    private void updateGoals() {
        mDbUsers.child("targetWeight").setValue("" + mSbTargetWeight.getProgress());
        mDbUsers.child("targetBMI").setValue("" + mSbTargetBMI.getProgress());
        mDbUsers.child("targetDailyCalories").setValue("" + mSbTargetCalories.getProgress());
        mDbUsers.child("targetHikes").setValue("" + mSbTargetHikes.getProgress());
        mDbUsers.child("weightGoal").setValue(mWeightGoal);
    }

    private boolean isValidData() {
        if (mTargetWeight.matches("") || mTargetBMI.matches("")
                || mTargetCalories.matches("") || mTargetHikes.matches("")
                || (rbWeightGoal == null)
                || (mWeightGoal = rbWeightGoal.getText().toString()).matches("")) {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

}