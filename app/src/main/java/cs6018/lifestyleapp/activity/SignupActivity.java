package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs6018.lifestyleapp.BuildConfig;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.utils.Logger;

public class SignupActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private String mUsername, mPassword;
    private CardView mCvLogin, mCvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);

        final Intent homeIntent = new Intent(this, HomeActivity.class);
        final Intent profileEnterIntent = new Intent(this, ProfileEnterActivity.class);

        mCvLogin = (CardView) findViewById(R.id.cv_login);
        mCvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername = mEtUsername.getText().toString();
                mPassword = mEtPassword.getText().toString();
                if (UserMatch(mUsername, mPassword)) { // TODO: Check if username:password in db
                    if (BuildConfig.DEBUG) {
                        Logger.log("Login Success - username: " + mUsername
                                + ", password: " + mPassword);
                    }
                    startActivity(profileEnterIntent);
                } else {
                    if (BuildConfig.DEBUG) {
                        Logger.log("Login Fail - username: " + mUsername
                                + ", password: " + mPassword);
                    }
                    // Toast.makeText(getApplication(), "Invalid Username/Password, please try again!", Toast.LENGTH_SHORT).show();
                    startActivity(profileEnterIntent);
                }
            }
        });

        mCvSignup = (CardView) findViewById(R.id.cv_signup);
        mCvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername = mEtUsername.getText().toString();
                mPassword = mEtPassword.getText().toString();
                if (!UserInDB(mUsername)) {
                    addToDB(mUsername, mPassword); // TODO: add username:password to db
                    if (BuildConfig.DEBUG) {
                        Logger.log("Signup Success - username: " + mUsername
                                + ", password: " + mPassword);
                    }
                    startActivity(profileEnterIntent);
                } else {
                    Logger.log("Signup Fail - username: " + mUsername
                            + ", password: " + mPassword);
                    Toast.makeText(getApplication(), "Username already existed, please try a new name!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean UserMatch(String username, String password) {
        return true;
    }

    private boolean UserInDB(String username) {
        return false;
    }

    private void addToDB(String username, String password) {

    }

}