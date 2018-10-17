package cs6018.lifestyleapp.Impl;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cs6018.lifestyleapp.Data.User;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.Utils.JSONProfileUtils;
import cs6018.lifestyleapp.ViewModel.ProfileViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment implements View.OnClickListener {

    private TextView mTvUserName,  mTvAge, mTvSex, mTvCity, mTvNation, mTvHeight, mTvWeight;

    private Button mBtEdit;

    private ProfileViewModel mProfileViewModel;

    private User mUser = User.getInstance();

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

        //Create the view model
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        mProfileViewModel.getData().observe(this, nameObserver);

        loadProfileData(mUser.getUserName(), JSONProfileUtils.toProfileJSonData(mUser));

        return view;
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null && isValidData()) {
                mTvUserName.setText(user.getUserName());
                mTvAge.setText(user.getAge());
                mTvSex.setText(user.getSex());
                mTvHeight.setText(user.getHeight());
                mTvWeight.setText(user.getWeight());
                mTvNation.setText(user.getNation());
                mTvCity.setText(user.getCity());
            }

        }
    };

    void loadProfileData(String userName, String profileJSon) {
        //pass the user in to the view model
        mProfileViewModel.setUser(userName, profileJSon);
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        if (isValidData()) {

            Fragment editProfileFrag = new EditProfileFrag();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.main_container, editProfileFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    private boolean isValidData() {
        if (mTvUserName.getText().toString().matches("")
                || mTvCity.getText().toString().matches("")
                || mTvAge.getText().toString().matches("")
                || mTvHeight.getText().toString().matches("")
                || mTvWeight.getText().toString().matches("")
                || mTvNation.getText().toString().matches("")) {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }

//        if (!mTvAge.getText().toString().matches("^(0|[1-9][0-9]*)$")
//                || !mTvHeight.getText().toString().matches("^(0|[1-9][0-9]*)$")
//                || !mTvWeight.getText().toString().matches("^(0|[1-9][0-9]*)$")) {
//            Toast toast = Toast.makeText(getActivity(),
//                    "Please enter numbers for the height, weight, target hikes, target BMI, and target calories fields", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
//            return false;
//        }
        return true;
    }
}
