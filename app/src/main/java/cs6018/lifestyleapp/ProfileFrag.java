package cs6018.lifestyleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment {

    private TextView mTvUserName;
    private TextView mTvAge;
    private TextView mTvSex;
    private TextView mTvCity;
    private TextView mTvNation;
    private TextView mTvHeight;
    private TextView mTvWeight;

    private Button mBtUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mTvUserName = (TextView) view.findViewById(R.id.tv_userName_data);
        mTvAge = (TextView) view.findViewById(R.id.tv_age_data);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex_data);
        mTvCity = (TextView) view.findViewById(R.id.tv_city_data);
        mTvNation = (TextView) view.findViewById(R.id.tv_nation_data);
        mTvHeight = (TextView) view.findViewById(R.id.tv_height_data);
        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_data);

//        mBtUpdate = (Button) view.findViewById(R.id.bt_update_profile);
//        mBtUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Todo
//            }
//        });

        //Get the data that was sent in
        // e.g String userName = getArguments().getString("UN_DATA");

        mTvUserName.setText("ABC");
        mTvAge.setText("22");
        mTvSex.setText("Male");
        mTvCity.setText("Salt Lake City");
        mTvNation.setText("US");
        mTvHeight.setText("6'0");
        mTvWeight.setText("195");

        return view;
    }
}
