package cs6018.lifestyleapp;

import android.support.v4.app.FragmentTransaction;
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
public class ProfileFrag extends Fragment implements View.OnClickListener {

    private String mUserName, mAge, mSex, mHeight, mWeight, mNation, mCity, mCurrentPhotoPath;

    private TextView mTvUserName;
    private TextView mTvAge;
    private TextView mTvSex;
    private TextView mTvCity;
    private TextView mTvNation;
    private TextView mTvHeight;
    private TextView mTvWeight;

    private Button mBtEdit;

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

        mBtEdit = (Button) view.findViewById(R.id.bt_edit_profile);
        mBtEdit.setOnClickListener(this);

        // Get the data that was sent in from HomeActivity
        getDataFromHomeActivity();

        // Set the usaer profile
        setProfileData();

        return view;
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        Fragment editProfileFrag = new EditProfileFrag();

        // Give editProfile an argument from the profileFrag
        Bundle profileBundle = new Bundle();
        passProfileDataToEditProfile(profileBundle);
        editProfileFrag.setArguments(profileBundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_container, editProfileFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * Get the data that was sent in from HomeActivity
     */
    private void getDataFromHomeActivity() {
        mUserName = getArguments().getString("item_username");
        mAge = getArguments().getString("item_age");
        mSex = getArguments().getString("item_sex");
        mHeight = getArguments().getString("item_height");
        mWeight = getArguments().getString("item_weight");
        mNation = getArguments().getString("item_nation");
        mCity = getArguments().getString("item_city");
        mCurrentPhotoPath = getArguments().getString("item_pic");
    }

    /*
     * Set the profile data
     */
    private void setProfileData() {
        mTvUserName.setText(mUserName);
        mTvAge.setText(mAge);
        mTvSex.setText(mSex);
        mTvHeight.setText(mHeight);
        mTvWeight.setText(mWeight);
        mTvNation.setText(mNation);
        mTvCity.setText(mCity);
    }

    private void passProfileDataToEditProfile(Bundle profileBundle) {
        profileBundle.putString("item_username", mUserName);
        profileBundle.putString("item_age", mAge);
        profileBundle.putString("item_sex", mSex);
        profileBundle.putString("item_height", mHeight);
        profileBundle.putString("item_weight", mWeight);
        profileBundle.putString("item_nation", mNation);
        profileBundle.putString("item_city", mCity);
        profileBundle.putString("item_pic", mCurrentPhotoPath);
    }
}
