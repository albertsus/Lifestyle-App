package cs6018.lifestyleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
    private String mTargetWeight, mTargetBMI, mTargetCalories, mTargetHikes;
    private TextView mTvWeight, mTvBMI, mTvHikes, mTvCalories;
    private TextView mTvTargetWeight, mTvTargetBMI, mTvTargetCalories, mTvTargetHikes;

    private SeekBar sbWeight, sbBMI, sbHikes, sbCalories;

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

        getData();

        setTargetData();

        setProgress();

        return view;
    }

    private void getData() {
        mWeight = getArguments().getString("item_weight");
        mBMI = getArguments().getString("item_bmi");
        mCalories = getArguments().getString("item_calories");
        mHikes = getArguments().getString("item_hikes");
        // Todo: replace the hard-coded data here to real data
        mHikes = "2";
        mCalories = "500";

        mTargetWeight = getArguments().getString("target_weight");
        mTargetBMI = getArguments().getString("target_bmi");
        mTargetCalories = getArguments().getString("target_calories");
        mTargetHikes = getArguments().getString("target_hikes");

    }

    private void setTargetData() {
        mTvTargetWeight.setText(mTargetWeight);
        mTvTargetBMI.setText(mTargetBMI);
        mTvTargetCalories.setText(mTargetCalories);
        mTvTargetHikes.setText(mTargetHikes);
    }

    private void setProgress() {
        setBar(sbWeight, mTvWeight, mTargetWeight, mWeight);
        setBar(sbBMI, mTvBMI, mTargetBMI, mBMI);
        setBar(sbHikes, mTvHikes, mTargetHikes, mHikes);
        setBar(sbCalories, mTvCalories, mTargetCalories, mCalories);
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
