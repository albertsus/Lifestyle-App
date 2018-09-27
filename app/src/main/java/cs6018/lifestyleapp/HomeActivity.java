package cs6018.lifestyleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    final Fragment goalsFrag = new GoalsFrag();
    final Fragment statsFrag = new StatsFrag();
    final Fragment toolsFrag = new ToolsFrag();
    final Fragment profileFrag = new ProfileFrag();
    final FragmentManager fragBoss = getSupportFragmentManager();
    Fragment active = profileFrag;
    // User user = new User();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stats:

                    fragBoss.beginTransaction().hide(active).show(statsFrag).commit();
                    active = statsFrag;
                    return true;
                case R.id.navigation_goals:

                    fragBoss.beginTransaction().hide(active).show(goalsFrag).commit();
                    active = goalsFrag;
                    return true;
                case R.id.navigation_profile:

                    fragBoss.beginTransaction().hide(active).show(profileFrag).commit();
                    active = profileFrag;
                    return true;
                case R.id.navigation_tools:

                    fragBoss.beginTransaction().hide(active).show(toolsFrag).commit();
                    active = toolsFrag;
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

        fragBoss.beginTransaction().add(R.id.main_container, toolsFrag, "4").hide(toolsFrag).commit();
        fragBoss.beginTransaction().add(R.id.main_container, profileFrag, "3").hide(profileFrag).commit();
        fragBoss.beginTransaction().add(R.id.main_container, statsFrag, "2").hide(statsFrag).commit();
        fragBoss.beginTransaction().add(R.id.main_container,goalsFrag, "1").commit();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
//        if (findViewById(R.id.main_fragment_container) != null) {
//
//            // Handle restoring from a previous state.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            toolsFrag.setRetainInstance(true);
//            profileFrag.setRetainInstance(true);
//            goalsFrag.setRetainInstance(true);
//            statsFrag.setRetainInstance(true);
//
//        }
    }

}
