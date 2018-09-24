package cs6018.lifestyleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    GoalsFrag goalsFrag = new GoalsFrag();
    StatsFrag statsFrag = new StatsFrag();
    ToolsFrag toolsFrag = new ToolsFrag();
    ProfileFrag profileFrag = new ProfileFrag();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stats:

                    return true;
                case R.id.navigation_goals:
                    //mTextMessage.setText("Goals");
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_profile);
                    return true;
                case R.id.navigation_tools:
                    //mTextMessage.setText(R.string.title_stats);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.main_fragment_container, toolsFrag).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.main_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            toolsFrag.setRetainInstance(true);
            profileFrag.setRetainInstance(true);
            goalsFrag.setRetainInstance(true);
            statsFrag.setRetainInstance(true);
        }

    }

}
