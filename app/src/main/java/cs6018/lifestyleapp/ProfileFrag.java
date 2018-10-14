package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
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

    private TextView mTvUserName,  mTvAge, mTvSex, mTvCity, mTvNation, mTvHeight, mTvWeight;

    private Button mBtEdit;

    private ProfileViewModel mProfileViewModel;

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

        loadProfileData(User.getInstance());

        return view;
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
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

    void loadProfileData(User user) {
        //pass the user in to the view model
        mProfileViewModel.setUser(user);
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        Fragment editProfileFrag = new EditProfileFrag();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_container, editProfileFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
