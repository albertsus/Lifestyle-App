package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cs6018.lifestyleapp.Utils.JSONProfileUtils;

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

    private static final int MIN_WEIGHT = 50, MAX_WEIGHT = 600;
    private static final int MIN_BMI = 5, MAX_BMI = 50;
    private static final int MIN_CALORIES = 100, MAX_CALORIES = 5000;
    private static final int MIN_HIKES = 0, MAX_HIKES = 10;

    private ProfileViewModel mProfileViewModel;

    private User mUser = User.getInstance();

    public EditGoalsFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_goals, container, false);

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

        spinnerWeightGoal = view.findViewById(R.id.spinner_weight_goal);

        mBtUpdate = (Button) view.findViewById(R.id.bt_update_goals);
        mBtUpdate.setOnClickListener(this);

        spinnerWeightGoal = (Spinner) view.findViewById(R.id.spinner_weight_goal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weigth_goal_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerWeightGoal.setAdapter(adapter);

        //Create the view model
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        mProfileViewModel.getData().observe(this, nameObserver);

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

                    loadProfileData(mUser.getUserName(), JSONProfileUtils.toProfileJSonData(mUser));

                    // Route to ProfileFrag
                    getFragmentManager().popBackStackImmediate();
                }
        }
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                System.out.println("Edit GoalsFrag");
            }
        }
    };

    void loadProfileData(String userName, String profileJSon) {
        //pass the user in to the view model
        mProfileViewModel.setUser(userName, profileJSon);
    }

    /**
     * Init the goals data when first in the fragment
     */
    private void InitGoalsData() {
        // Set the profile data
        mTargetWeight = mUser.getTargetWeight();
        mTargetBMI = mUser.getTargetBMI();
        mTargetCalories = mUser.getTargetDailyCalories();
        mTargetHikes = mUser.getTargetHikes();

        mTvMinWeight.setText("" + MIN_WEIGHT);
        mTvMaxWeight.setText("" + MAX_WEIGHT);
        mTvMinBMI.setText("" + MIN_BMI);
        mTvMaxBMI.setText("" + MAX_BMI);
        mTvMinCalories.setText("" + MIN_CALORIES);
        mTvMaxCalories.setText("" + MAX_CALORIES);
        mTvMinHikes.setText("" + MIN_HIKES);
        mTvMaxHikes.setText("" + MAX_HIKES);

        setBar(mSbTargetWeight, mTvTargetWeight, Integer.valueOf(mUser.getTargetWeight()), MIN_WEIGHT, MAX_WEIGHT);
        setBar(mSbTargetBMI, mTvTargetBMI, Integer.valueOf(mUser.getTargetBMI()), MIN_BMI, MAX_BMI);
        setBar(mSbTargetCalories, mTvTargetCalories, Integer.valueOf(mUser.getTargetDailyCalories()), MIN_CALORIES, MAX_CALORIES);
        setBar(mSbTargetHikes, mTvTargetHikes, Integer.valueOf(mUser.getTargetHikes()), MIN_HIKES, MAX_HIKES);
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

    private boolean isValidData() {
        if (mTargetWeight.matches("") || mTargetBMI.matches("")
                || mTargetCalories.matches("") || mTargetHikes.matches("")) {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * Update the goals once click 'Edit' Button
     */
    private void updateGoals() {
        mUser.setTargetWeight(String.valueOf(mSbTargetWeight.getProgress()));
        mUser.setTargetBMI(String.valueOf(mSbTargetBMI.getProgress()));
        mUser.setTargetDailyCalories(String.valueOf(mSbTargetCalories.getProgress()));
        mUser.setTargetHikes(String.valueOf(mSbTargetHikes.getProgress()));
        mUser.setWeightGoal((String) spinnerWeightGoal.getSelectedItem());
    }
}