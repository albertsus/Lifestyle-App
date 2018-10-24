package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.fragment.ProfileEnterFrag;

/**
 * Created by suchaofan on 10/20/18.
 */

public class ProfileEnterActivity extends AppCompatActivity implements
        ProfileEnterFrag.OnFloatingButtonClickListener {

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
    public void onFloatingButtonClicked() {
        // Start HomeActivity
        this.startActivity(new Intent(this, HomeActivity.class));
    }

}
