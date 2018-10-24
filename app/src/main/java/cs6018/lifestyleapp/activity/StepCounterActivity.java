package cs6018.lifestyleapp.activity;

<<<<<<< HEAD
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import cs6018.lifestyleapp.R;
import android.widget.ImageButton;
import android.widget.TextView;

public class StepCounterActivity extends AppCompatActivity implements View.OnClickListener{

    boolean debug = true;

    private TextView mTvStepCount;

    private ImageButton mBtnStart, mBtnStop, mBtnReset;

    private Integer mStepCount;
    private Integer mStartCount = 0;

    MediaPlayer mediaPlayer = new MediaPlayer();
    //runs without a timer by reposting this handler at the end of the runnable
    Handler countHandler = new Handler();

    Runnable countRunnable = new Runnable() {

        @Override
        public void run() {
            mStepCount += 1;
            countHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_file_1);

        mTvStepCount = findViewById(R.id.tv_bmr_data);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(this);

        mBtnStop = findViewById(R.id.btn_stop);
        mBtnStop.setOnClickListener(this);

        mBtnReset = findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(this);

        mTvStepCount.setText(Integer.toString(mStepCount));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start: {
                countHandler.postDelayed(countRunnable, 0);
                break;
            }

            case R.id.btn_stop: {
                countHandler.removeCallbacks(countRunnable);
                break;
            }

            case R.id.btn_reset: {
                mTvStepCount.setText("0");
                countHandler.postDelayed(countRunnable, 0);
                break;
            }

        }
    }
=======
public class StepCounterActivity {

>>>>>>> 4b0db674b63eb115d94b512b49d15964377080e4

}


