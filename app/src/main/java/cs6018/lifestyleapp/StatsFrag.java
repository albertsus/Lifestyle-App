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
 * create an instance of this fragment.
 */
public class StatsFrag extends Fragment {

    private TextView mTvWeight;
    private TextView mTvBMI;
    private TextView mTvBMR;
    private TextView mTvHikes;
    private TextView mTvCalories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_data);
        mTvBMI = (TextView) view.findViewById(R.id.tv_bmi_data);
        mTvBMR = (TextView) view.findViewById(R.id.tv_bmr_data);
        mTvCalories = (TextView) view.findViewById(R.id.tv_calories_data);
        mTvHikes = (TextView) view.findViewById(R.id.tv_hikes_data);


        String weight = getArguments().getString("item_weight");
        String bmi = getArguments().getString("item_bmi");
        String bmr = getArguments().getString("item_bmr");
        String calories = getArguments().getString("item_calories");
        String hikes = getArguments().getString("item_hikes");

        mTvWeight.setText(weight);
        mTvBMI.setText(bmi);
        mTvBMR.setText(bmr);
        mTvCalories.setText(calories);
        mTvHikes.setText(hikes);

        return view;
    }
}
