package cs6018.lifestyleapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFrag extends Fragment implements View.OnClickListener{

    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes, mTvWeightGoal;

    private Button mBtEdit;

    private DatabaseReference mDbUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mTvTargetWeight = (TextView) view.findViewById(R.id.tv_target_weight_data);
        mTvTargetBMI = (TextView) view.findViewById(R.id.tv_target_bmi_data);
        mTvTargetCalories = (TextView) view.findViewById(R.id.tv_target_calories_data);
        mTvTargetHikes = (TextView) view.findViewById(R.id.tv_target_hikes_data);
        mTvWeightGoal = (TextView) view.findViewById(R.id.tv_weight_goal_data);

        mBtEdit = (Button) view.findViewById(R.id.bt_edit_goals);
        mBtEdit.setOnClickListener(this);

        fillTargetInfo();

        return view;
    }

    private void fillTargetInfo() {
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(User.getUUID());
                mTvTargetWeight.setText(ds.child("targetWeight").getValue(String.class));
                mTvTargetBMI.setText(ds.child("targetBMI").getValue(String.class));
                mTvTargetHikes.setText(ds.child("targetHikes").getValue(String.class));
                mTvTargetCalories.setText(ds.child("targetDailyCalories").getValue(String.class));
                mTvWeightGoal.setText(ds.child("weightGoal").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
