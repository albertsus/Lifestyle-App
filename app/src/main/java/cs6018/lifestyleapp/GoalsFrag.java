package cs6018.lifestyleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFrag extends Fragment {

    private TextView mTvTargetWeight;
    private TextView mTvTargetBMI;
    private TextView mTvTargetCalories;
    private TextView mTvTargetHikes;
    private TextView mTvWeightGoal;

    private Button mBtUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        mTvTargetWeight = (TextView) view.findViewById(R.id.tv_target_weight_data);
        mTvTargetBMI = (TextView) view.findViewById(R.id.tv_target_bmi_data);
        mTvTargetCalories = (TextView) view.findViewById(R.id.tv_target_calories_data);
        mTvTargetHikes = (TextView) view.findViewById(R.id.tv_target_hikes_data);
        mTvWeightGoal = (TextView) view.findViewById(R.id.tv_weight_goal_data);


        String targetWeight = getArguments().getString("target_weight");
        String targetBMI = getArguments().getString("target_bmi");
        String targetCalories = getArguments().getString("target_calories");
        String targetHikes = getArguments().getString("target_hikes");
        String weightGoal = getArguments().getString("weight_goal");

        mTvTargetWeight.setText(targetWeight);
        mTvTargetBMI.setText(targetBMI);
        mTvTargetCalories.setText(targetCalories);
        mTvTargetHikes.setText(targetHikes);
        mTvWeightGoal.setText(weightGoal);

        return view;
    }
}
