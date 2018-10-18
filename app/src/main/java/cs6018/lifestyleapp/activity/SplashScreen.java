package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.activity.SignupActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    private final Handler mHandler   = new Handler();
    private final Launcher mLauncher = new Launcher();

    @Override
    protected void onStart() {
        super.onStart();

        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }

    private void launch() {
        if (!isFinishing()) {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }
    }

    private class Launcher implements Runnable {
        @Override
        public void run() {
            launch();
        }
    }
}