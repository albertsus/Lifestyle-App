package cs6018.lifestyleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by suchaofan on 9/29/18.
 */

public class EditGoalsFrag extends Fragment implements View.OnClickListener {

    private User userGoals = new User();

    private String mTargetWeight, mTargetBMI, mTargetCalories, mTargetHikes, mWeightGoal;

    private EditText mEtTargetWeight, mEtTargetBMI, mEtTargetCalories, mEtTargetHikes;
    private Spinner spinnerWeightGoal;
    private Button mBtUpdate;

    private OnUserGoalsPass userGoalsPasser;

    public EditGoalsFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_goals, container, false);

        mEtTargetWeight = view.findViewById(R.id.et_target_weight);
        mEtTargetBMI = view.findViewById(R.id.et_target_bmi);
        mEtTargetCalories = view.findViewById(R.id.et_target_calories);
        mEtTargetHikes = view.findViewById(R.id.et_target_hikes);
        spinnerWeightGoal = view.findViewById(R.id.spinner_weight_goal);

        mBtUpdate = (Button) view.findViewById(R.id.bt_update_goals);
        mBtUpdate.setOnClickListener(this);

        spinnerWeightGoal = (Spinner) view.findViewById(R.id.spinner_weight_goal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weigth_goal_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerWeightGoal.setAdapter(adapter);

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

                    // Pass data to HomeActivity and update user goals
                    passGoalsData(userGoals);

                    // Route to ProfileFrag
                    getFragmentManager().popBackStackImmediate();
                }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userGoalsPasser = (OnUserGoalsPass) context;
    }

    public void passGoalsData(User userGoals) {
        userGoalsPasser.passGoalsData(userGoals);
    }

    public interface OnUserGoalsPass {
        void passGoalsData(User userGoals);
    }


    /**
     * Init the goals data when first in the fragment
     */
    private void InitGoalsData() {
        // Get data from GoalsFrag
        mTargetWeight = getArguments().getString("target_weight");
        mTargetBMI = getArguments().getString("target_bmi");
        mTargetCalories = getArguments().getString("target_calories");
        mTargetHikes = getArguments().getString("target_hikes");
        mWeightGoal = getArguments().getString("weight_goal");

        // Set the profile data
        mEtTargetWeight.setText(mTargetWeight);
        mEtTargetBMI.setText(mTargetBMI);
        mEtTargetCalories.setText(mTargetCalories);
        mEtTargetHikes.setText(mTargetHikes);
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
        mTargetWeight = mEtTargetWeight.getText().toString();
        mTargetBMI = mEtTargetBMI.getText().toString();
        mTargetCalories = mEtTargetCalories.getText().toString();
        mTargetHikes = mEtTargetHikes.getText().toString();
        mWeightGoal = (String) spinnerWeightGoal.getSelectedItem();

        userGoals.setTargetWeight(mTargetWeight);
        userGoals.setTargetBMI(mTargetBMI);
        userGoals.setTargetDailyCalories(mTargetCalories);
        userGoals.setTargetHikes(mTargetHikes);
        userGoals.setWeightGoal(mWeightGoal);
    }
}
