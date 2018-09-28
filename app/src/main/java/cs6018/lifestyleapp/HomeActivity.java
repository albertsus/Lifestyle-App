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
import android.view.View;

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

    }

    /** Handles the tools fragment buttons
     *
     * @param view the current view.
     */
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.frame_weather: {
                Intent weatherIntent = new Intent(this, WeatherActivity.class);
                startActivity(weatherIntent);
                break;
            }

            case R.id.frame_hike: {
                Intent hikeIntent = new Intent(this, HikeActivity.class);
                startActivity(hikeIntent);
                break;
            }

            case R.id.frame_calculator: {
                Intent calculatorIntent = new Intent(this, CalculatorActivity.class);
                startActivity(calculatorIntent);
                break;
            }
        }
    }

}
