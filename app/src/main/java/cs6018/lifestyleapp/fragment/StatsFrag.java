package cs6018.lifestyleapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.utils.JSONProfileUtils;
import cs6018.lifestyleapp.viewModel.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StatsFrag extends Fragment {

    private TextView mTvWeight, mTvBMI, mTvHikes, mTvCalories;
    private TextView mTvStartWeight, mTvStartBMI, mTvStartCalories, mTvStartHikes;
    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes;

    private SeekBar sbWeight, sbBMI, sbHikes, sbCalories;

    // private ProfileViewModel mProfileViewModel;

    // private User mUser = User.getInstance();

    private DatabaseReference mDbUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());

        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_data);
        mTvBMI = (TextView) view.findViewById(R.id.tv_bmi_data);
        mTvCalories = (TextView) view.findViewById(R.id.tv_calories_data);
        mTvHikes = (TextView) view.findViewById(R.id.tv_hikes_data);

        mTvStartWeight = (TextView) view.findViewById(R.id.tv_weight_start);
        mTvStartBMI = (TextView) view.findViewById(R.id.tv_bmi_start);
        mTvStartCalories = (TextView) view.findViewById(R.id.tv_calories_start);
        mTvStartHikes = (TextView) view.findViewById(R.id.tv_hikes_start);

        mTvTargetWeight = (TextView) view.findViewById(R.id.tv_weight_end);
        mTvTargetBMI = (TextView) view.findViewById(R.id.tv_bmi_end);
        mTvTargetCalories = (TextView) view.findViewById(R.id.tv_calories_end);
        mTvTargetHikes = (TextView) view.findViewById(R.id.tv_hikes_end);

        sbWeight = (SeekBar) view.findViewById(R.id.sb_weight);
        sbBMI = (SeekBar) view.findViewById(R.id.sb_bmi);
        sbHikes = (SeekBar) view.findViewById(R.id.sb_hikes);
        sbCalories = (SeekBar) view.findViewById(R.id.sb_calories);

        //Create the view model
        // mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        // mProfileViewModel.getData().observe(this, nameObserver);

        // loadProfileData(mUser.getUserName(), JSONProfileUtils.toProfileJSonData(mUser));

        fillStatsInfo();

        return view;
    }

    //create an observer that watches the LiveData<User> object
//    final Observer<User> nameObserver = new Observer<User>() {
//        @Override
//        public void onChanged(@Nullable final User user) {
//            // Update the UI if this data variable changes
//            if (user != null) {
//                setProgress(user);
//            }
//        }
//    };

//    void loadProfileData(String userName, String profileJSon) {
//        //pass the user in to the view model
//        mProfileViewModel.setUser(userName, profileJSon);
//    }

    private void fillStatsInfo() {
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String weight = dataSnapshot.child("weight").getValue(String.class);
                String bmi = dataSnapshot.child("bmi").getValue(String.class);
                // String hikes = dataSnapshot.child("hikes").getValue(String.class);
                String hikes = "0";
                String calories = dataSnapshot.child("calories").getValue(String.class);

                String targetWeight = dataSnapshot.child("targetWeight").getValue(String.class);
                String targetBMI = dataSnapshot.child("targetBMI").getValue(String.class);
                String targetHikes = dataSnapshot.child("targetHikes").getValue(String.class);
                String targetDailyCalories = dataSnapshot.child("targetDailyCalories").getValue(String.class);

                String startWeight = dataSnapshot.child("startWeight").getValue(String.class);
                String startBMI = dataSnapshot.child("startBMI").getValue(String.class);
                String startHikes = dataSnapshot.child("startHikes").getValue(String.class);
                String startCalories = dataSnapshot.child("startCalories").getValue(String.class);

                setProgress(weight, bmi, hikes, calories,
                        targetWeight, targetBMI, targetHikes, targetDailyCalories,
                        startWeight, startBMI, startHikes, startCalories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProgress(String w, String b, String h, String c,
                             String tw, String tb, String th, String tc,
                             String sw, String sb, String sh, String sc) {
        setBar(sbWeight, mTvWeight, mTvStartWeight, mTvTargetWeight, sw, tw, w);
        setBar(sbBMI, mTvBMI, mTvStartBMI, mTvTargetBMI, sb, tb, b);
        setBar(sbHikes, mTvHikes, mTvStartHikes, mTvTargetHikes, sh, th, h);
        setBar(sbCalories, mTvCalories, mTvStartCalories, mTvTargetCalories, sc, tc, c);
    }

    private void setBar(SeekBar sb, final TextView tvProg, TextView tvStart, TextView tvGoal,
                        String startStr, String goalStr, String progressStr) {
        final int start = Integer.valueOf(startStr),
            goal = Integer.valueOf(goalStr),
            progress = Integer.valueOf(progressStr);

        float pos;
        if (start < goal) {
            tvStart.setText("start [" + startStr + "]");
            tvGoal.setText("goal [" + goalStr + "]");
            sb.setMax(goal - start);
            sb.setProgress(progress - start);
            tvProg.setText("" + progress);
            float progDiff = (progress > goal ? goal : progress) - start;
            float totalDiff = goal - start;
            pos = progress > goal ? 815 : (progDiff / totalDiff * 915);
            tvProg.setX(pos);
        } else {
            tvGoal.setText("start [" + startStr + "]");
            tvStart.setText("goal [" + goalStr + "]");
            sb.setMax(start - goal);
            sb.setProgress(progress - goal);
            tvProg.setText("" + progress);
            float progDiff = (progress > start ? start : progress) - goal;
            float totalDiff = start - goal;
            pos = progress > start ? 815 : (progDiff / totalDiff * 915);
            tvProg.setX(pos);
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvProg.setVisibility(View.VISIBLE);
                seekBar.setProgress(progress - start);
            }
        });

//        int val = (sb.getProgress() * (sb.getWidth() - 2 * sb.getThumbOffset())) / sb.getMax();
//        tvProg.setText("" + progress);
//        tvProg.setX(sb.getX() + val + sb.getThumbOffset() / 2);

//        int val = (sb.getProgress() * (950 - 2 * 24)) / sb.getMax();
//        final int Pos = val + 180 + 24 / 2;
//        tvProg.setText("" + sb.getProgress());
//        tvProg.setX(Pos - 270);

    }
}
