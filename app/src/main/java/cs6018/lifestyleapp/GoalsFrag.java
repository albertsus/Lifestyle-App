package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes, mTvWeightGoal;

    private Button mBtEdit;

    private ProfileViewModel mProfileViewModel;

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

        //Create the view model
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        mProfileViewModel.getData().observe(this, nameObserver);

        loadProfileData(User.getInstance());

        return view;
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                mTvTargetWeight.setText(user.getTargetWeight());
                mTvTargetBMI.setText(user.getTargetBMI());
                mTvTargetCalories.setText(user.getTargetDailyCalories());
                mTvTargetHikes.setText(user.getTargetHikes());
                mTvWeightGoal.setText(user.getWeightGoal());
            }
        }
    };

    void loadProfileData(User user) {
        //pass the user in to the view model
        mProfileViewModel.setUser(user);
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        Fragment editGoalsFrag = new EditGoalsFrag();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_container, editGoalsFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
