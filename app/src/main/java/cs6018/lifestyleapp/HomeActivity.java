package cs6018.lifestyleapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class HomeActivity extends AppCompatActivity {

    final Fragment goalsFrag = new GoalsFrag();
    final Fragment statsFrag = new StatsFrag();
    final Fragment toolsFrag = new ToolsFrag();
    final Fragment profileFrag = new ProfileFrag();
    final FragmentManager fragBoss = getSupportFragmentManager();
    Fragment active = statsFrag;

    User user = new User();

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

        // Retrieve user info from SignUp Activity
        dataRetrieve();

        // Pass user info to fragmentss
        dataPass();

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

    /*
     *  Retrieve user info from SignUp Activity
     */
    private void dataRetrieve() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user.setUserName(extras.getString("USERNAME"));
            user.setAge(extras.getString("AGE"));
            user.setSex(extras.getString("SEX"));
            user.setHeight(extras.getString("HEIGHT"));
            user.setWeight(extras.getString("WEIGHT"));
            user.setNation(extras.getString("NATION"));
            user.setCity(extras.getString("CITY"));
            user.setProfilePic(extras.getString("PROFILE_PIC"));

            user.setTargetWeight(extras.getString("TARGET_WEIGHT"));
            user.setTargetBMI(extras.getString("TARGET_BMI"));
            user.setTargetDailyCalories(extras.getString("TARGET_CALORIES"));
            user.setTargetHikes(extras.getString("TARGET_HIKES"));
            user.setWeightGoal(extras.getString("WEIGHT_GOAL"));

            user.setBmi(String.valueOf(CalculatorUtils.computeBMI(user.getWeight(), user.getHeight())));
            user.setBmr(String.valueOf(CalculatorUtils.computeBMR(user.getWeight(),
                    user.getHeight(), user.getSex(), user.getAge())));
        }
    }

    /*
     *  Pass user info to fragments
     */
    private void dataPass() {

        Bundle profileBundle = new Bundle();
        profileBundle.putString("item_username", user.getUserName());
        profileBundle.putString("item_age", user.getAge());
        profileBundle.putString("item_sex", user.getSex());
        profileBundle.putString("item_height", String.valueOf(user.getHeight()));
        profileBundle.putString("item_weight", String.valueOf(user.getWeight()));
        profileBundle.putString("item_nation", user.getNation());
        profileBundle.putString("item_city", user.getCity());
        profileBundle.putString("item_pic", user.getProfilePic());

        profileBundle.putString("item_bmi", user.getBmi());
        profileBundle.putString("item_bmr", user.getBmr());

        profileBundle.putString("target_weight", user.getTargetWeight());
        profileBundle.putString("target_bmi", user.getTargetBMI());
        profileBundle.putString("target_calories", user.getTargetDailyCalories());
        profileBundle.putString("target_hikes", user.getTargetHikes());
        profileBundle.putString("weight_goal", user.getWeightGoal());

        profileFrag.setArguments(profileBundle);
        goalsFrag.setArguments(profileBundle);
        statsFrag.setArguments(profileBundle);
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
        }
    }

}
