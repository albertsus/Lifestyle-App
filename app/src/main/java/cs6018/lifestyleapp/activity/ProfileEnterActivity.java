package cs6018.lifestyleapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cs6018.lifestyleapp.BuildConfig;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.fragment.ProfileEnterFrag;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.Logger;
import cs6018.lifestyleapp.viewModel.ProfileViewModel;

/**
 * Created by suchaofan on 10/20/18.
 */

public class ProfileEnterActivity extends AppCompatActivity implements
        ProfileEnterFrag.OnFloatingButtonClickListener {

    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_enter);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProfileEnterFrag())
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar= (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("PROFILE");
    }

    @Override
    public void onFloatingButtonClicked(String param1, String param2) {
        //Create new Intent Object, and specify class
        Intent homeActivity = new Intent(this, HomeActivity.class);

        //Create the view model
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        mProfileViewModel.getData().observe(this, nameObserver);

        loadProfileData(param1, param2);

        // Start the HomeActivity
        this.startActivity(homeActivity);
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                if (BuildConfig.DEBUG) {
                    Logger.log("Profile Updated");
                }
            } else {
                if (BuildConfig.DEBUG) {
                    Logger.log("Observe User is null");
                }
            }
        }
    };

    void loadProfileData(String userName, String profileJSon) {
        //pass the user in to the view model
        mProfileViewModel.setUser(userName, profileJSon);
    }
}
