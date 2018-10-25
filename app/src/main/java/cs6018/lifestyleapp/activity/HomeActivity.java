package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.fragment.GoalsFrag;
import cs6018.lifestyleapp.fragment.ProfileFrag;
import cs6018.lifestyleapp.fragment.StatsFrag;
import cs6018.lifestyleapp.fragment.ToolsFrag;

public class HomeActivity extends AppCompatActivity {

    final Fragment goalsFrag = new GoalsFrag();
    final Fragment statsFrag = new StatsFrag();
    final Fragment toolsFrag = new ToolsFrag();
    final Fragment profileFrag = new ProfileFrag();
    final FragmentManager fragBoss = getSupportFragmentManager();
    Fragment active = statsFrag;
    
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
        fragBoss.beginTransaction().add(R.id.main_container, goalsFrag, "2").hide(goalsFrag).commit();
        fragBoss.beginTransaction().add(R.id.main_container, statsFrag, "1").commit();

        // Customize the size of the bottom navigation component.
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                this.findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView)
                bottomNavigationView.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView =
                    menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams =
                    iconView.getLayoutParams();
            final DisplayMetrics displayMetrics =
                    getResources().getDisplayMetrics();
            layoutParams.height = (int)
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36,
                            displayMetrics);
            layoutParams.width = (int)
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36,
                            displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    /**
     * Handle the tools activity click events.
     *
     * @param view
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
            case R.id.frame_counter: {
                Intent pedometerIntent = new Intent(this, StepCounterActivity.class);
                startActivity(pedometerIntent);
                break;
            }
        }
    }
}
