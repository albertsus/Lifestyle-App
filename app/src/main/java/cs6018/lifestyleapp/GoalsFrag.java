package cs6018.lifestyleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFrag extends Fragment implements View.OnClickListener{

    private String mTargetWeight, mTargetBMI, mTargetCalories, mTargetHikes, mWeightGoal;

    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes, mTvWeightGoal;

    private Button mBtEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        mTvTargetWeight = (TextView) view.findViewById(R.id.tv_target_weight_data);
        mTvTargetBMI = (TextView) view.findViewById(R.id.tv_target_bmi_data);
        mTvTargetCalories = (TextView) view.findViewById(R.id.tv_target_calories_data);
        mTvTargetHikes = (TextView) view.findViewById(R.id.tv_target_hikes_data);
        mTvWeightGoal = (TextView) view.findViewById(R.id.tv_weight_goal_data);

        mBtEdit = (Button) view.findViewById(R.id.bt_edit_goals);
        mBtEdit.setOnClickListener(this);

        // Get the data that was sent in from HomeActivity
        getDataFromHomeActivity();

        // Set the usaer target data
        setTargetData();

        return view;
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        Fragment editGoalsFrag = new EditGoalsFrag();

        // Give editGoalsFrag an argument from the goalsFrag
        Bundle goalsBundle = new Bundle();
        passToEditTarget(goalsBundle);
        editGoalsFrag.setArguments(goalsBundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_container, editGoalsFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * Get the data that was sent in from HomeActivity
     */
    private void getDataFromHomeActivity() {
        mTargetWeight = getArguments().getString("target_weight");
        mTargetBMI = getArguments().getString("target_bmi");
        mTargetCalories = getArguments().getString("target_calories");
        mTargetHikes = getArguments().getString("target_hikes");
        mWeightGoal = getArguments().getString("weight_goal");
    }

    /*
     * Set the user target data
     */
    private void setTargetData() {
        mTvTargetWeight.setText(mTargetWeight);
        mTvTargetBMI.setText(mTargetBMI);
        mTvTargetCalories.setText(mTargetCalories);
        mTvTargetHikes.setText(mTargetHikes);
        mTvWeightGoal.setText(mWeightGoal);
    }

    private void passToEditTarget(Bundle goalsBundle) {
        goalsBundle.putString("target_weight", mTargetWeight);
        goalsBundle.putString("target_bmi", mTargetBMI);
        goalsBundle.putString("target_calories", mTargetCalories);
        goalsBundle.putString("target_hikes", mTargetHikes);
        goalsBundle.putString("weight_goal", mWeightGoal);
    }
}
