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

import org.w3c.dom.Text;


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


        //Get the data that was sent in
        String userName = getArguments().getString("item_username");
        String age = getArguments().getString("item_age");
        String sex = getArguments().getString("item_sex");
        String height = getArguments().getString("item_height");
        String weight = getArguments().getString("item_weight");
        String nation = getArguments().getString("item_nation");
        String city = getArguments().getString("item_city");
        String pic = getArguments().getString("item_pic");

        mTvUserName.setText(userName);
        mTvAge.setText(age);
        mTvSex.setText(sex);
        mTvHeight.setText(height);
        mTvWeight.setText(weight);
        mTvNation.setText(nation);
        mTvCity.setText(city);

        return view;
    }
}
