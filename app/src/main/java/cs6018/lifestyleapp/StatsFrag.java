package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StatsFrag extends Fragment {

    private String mWeight, mBMI, mHikes, mCalories;
    private TextView mTvWeight, mTvBMI, mTvHikes, mTvCalories;
    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes;

    private SeekBar sbWeight, sbBMI, sbHikes, sbCalories;

    private ProfileViewModel mProfileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_data);
        mTvBMI = (TextView) view.findViewById(R.id.tv_bmi_data);
        mTvCalories = (TextView) view.findViewById(R.id.tv_calories_data);
        mTvHikes = (TextView) view.findViewById(R.id.tv_hikes_data);

        mTvTargetWeight = (TextView) view.findViewById(R.id.tv_weight_end);
        mTvTargetBMI = (TextView) view.findViewById(R.id.tv_bmi_end);
        mTvTargetCalories = (TextView) view.findViewById(R.id.tv_calories_end);
        mTvTargetHikes = (TextView) view.findViewById(R.id.tv_hikes_end);

        sbWeight = (SeekBar) view.findViewById(R.id.sb_weight);
        sbBMI = (SeekBar) view.findViewById(R.id.sb_bmi);
        sbHikes = (SeekBar) view.findViewById(R.id.sb_hikes);
        sbCalories = (SeekBar) view.findViewById(R.id.sb_calories);

        mHikes = "2";
        mCalories = "500";

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
                setProgress(user);
            }
        }
    };

    void loadProfileData(User user) {
        //pass the user in to the view model
        mProfileViewModel.setUser(user);
    }

    private void setProgress(User user) {
        setBar(sbWeight, mTvWeight, user.getTargetWeight(), user.getWeight());
        setBar(sbBMI, mTvBMI, user.getTargetBMI(), user.getBmi());
        setBar(sbHikes, mTvHikes, user.getTargetHikes(), mHikes);
        setBar(sbCalories, mTvCalories, user.getTargetDailyCalories(), mCalories);
    }

    private void setBar(final SeekBar sb, final TextView tv, String maxStr, String progressStr) {
        final int max = Integer.valueOf(maxStr), progress = Integer.valueOf(progressStr);
        sb.setMax(max);
        sb.setProgress(progress);
        int val = (sb.getProgress() * (950 - 2 * 24)) / sb.getMax();
        final int Pos = val + 180 + 24 / 2;
        tv.setText("" + sb.getProgress());
        tv.setX(Pos - 270);

        // Tricks to fix the index thumb -> probably wrong
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int prog, boolean b) {
                sb.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
