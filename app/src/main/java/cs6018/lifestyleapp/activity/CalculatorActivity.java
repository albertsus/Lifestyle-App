package cs6018.lifestyleapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.JSONProfileUtils;
import cs6018.lifestyleapp.viewModel.ProfileViewModel;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvBMR, mTvBMI, mTvBMIPro, mTvBMIStart, mTvBMIEnd;

    private Button mBtnBMR, mBtnBMI;

    private SeekBar mSbBMI;

    private String mBmi, mBmr;

    static final int UNDERWEIGHT_BMI = 19;
    static final int OVERWEIGHT_BMI = 25;
    static final int MAXIMUM_BMI = 40;

    private DatabaseReference mDbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users");

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

        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(User.getUUID());
                mBmi = ds.child("bmi").getValue(String.class);
                mBmr = ds.child("bmr").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_bmr: {
                mTvBMR.setText(mBmr);
                break;
            }

            case R.id.btn_bmi: {
                mTvBMI.setText(mBmi);
                setProgress();
                break;
            }
        }
    }

    private void setProgress() {
        final int progress= Integer.valueOf(mBmi);
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
