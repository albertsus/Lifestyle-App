package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import cs6018.lifestyleapp.Data.User;
import cs6018.lifestyleapp.Utils.JSONProfileUtils;
import cs6018.lifestyleapp.ViewModel.ProfileViewModel;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvBMR, mTvBMI, mTvBMIPro, mTvBMIStart, mTvBMIEnd;

    private Button mBtnBMR, mBtnBMI;

    private SeekBar mSbBMI;

    static final int UNDERWEIGHT_BMI = 19;
    static final int OVERWEIGHT_BMI = 25;
    static final int MAXIMUM_BMI = 40;

    private ProfileViewModel mProfileViewModel;

    private User mUser = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mTvBMR = findViewById(R.id.tv_bmr_data);
        mTvBMI = findViewById(R.id.tv_bmi_data);
        mTvBMIPro = findViewById(R.id.tv_bmi_pro);
        mTvBMIStart = findViewById(R.id.tv_bmi_start);
        mTvBMIEnd = findViewById(R.id.tv_bmi_end);

        mSbBMI = findViewById(R.id.sb_bmi);

        mBtnBMR = findViewById(R.id.btn_bmr);
        mBtnBMR.setOnClickListener(this);

        mBtnBMI = findViewById(R.id.btn_bmi);
        mBtnBMI.setOnClickListener(this);

        //Create the view model
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        //Set the observer
        mProfileViewModel.getData().observe(this, nameObserver);

        loadProfileData(mUser.getUserName(), JSONProfileUtils.toProfileJSonData(mUser));
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                System.out.println("CalculatorActivity");
            }
        }
    };

    void loadProfileData(String userName, String profileJSon) {
        //pass the user in to the view model
        mProfileViewModel.setUser(userName, profileJSon);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_bmr: {
                mTvBMR.setText(mUser.getBmr());
                break;
            }

            case R.id.btn_bmi: {
                mTvBMI.setText(mUser.getBmi());
                setProgress();
                break;
            }
        }
    }

    private void setProgress() {
        final int progress= Integer.valueOf(mUser.getBmi());
        mSbBMI.setMax(MAXIMUM_BMI);
        mSbBMI.setProgress(progress);
        int val = (progress * (950 - 2 * 24)) / MAXIMUM_BMI;
        final int Pos = val + 180 + 24 / 2;
        mTvBMIStart.setText(""+UNDERWEIGHT_BMI);
        mTvBMIEnd.setText(""+OVERWEIGHT_BMI);
        mTvBMIPro.setX(Pos - 200);
        mTvBMIPro.setText("" + progress);

        // Tricks to fix the index thumb -> probably wrong
        mSbBMI.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int prog, boolean b) {
                mSbBMI.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
