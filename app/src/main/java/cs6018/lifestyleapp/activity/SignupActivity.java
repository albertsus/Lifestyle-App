package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs6018.lifestyleapp.BuildConfig;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.utils.Logger;

public class SignupActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private String mNewUsername, mNewUserPassword;
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignupActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) dialogView.findViewById(R.id.etEmail);
                final EditText mPassword = (EditText) dialogView.findViewById(R.id.etPassword);
                Button mLogin = (Button) dialogView.findViewById(R.id.btnLogin);
                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mUn = mEmail.getText().toString();
                        String mPwd = mPassword.getText().toString();
                        if (UserMatch(mUn, mPwd)) { // TODO: Check if username:password in db
                            if (BuildConfig.DEBUG) {
                                Log.v("Login", "Login Success - username: " + mUn
                                        + ", password: " + mPwd);
                            }
                            dialog.dismiss();
                            startActivity(profileEnterIntent);
                        } else {
                            if (BuildConfig.DEBUG) {
                                Log.v("Login", "Login Fail - username: " + mUn
                                        + ", password: " + mPwd);
                            }
                            Toast.makeText(getApplication(), "Invalid Username/Password, please try again!", Toast.LENGTH_SHORT).show();
                            //startActivity(profileEnterIntent);
                        }
                    }
                });
            }
        });

        mCvSignup = (CardView) findViewById(R.id.cv_signup);
        mCvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewUsername = mEtUsername.getText().toString();
                mNewUserPassword = mEtPassword.getText().toString();
                if (!UserInDB(mNewUsername)) {
                    addToDB(mNewUsername, mNewUserPassword); // TODO: add username:password to db
                    if (BuildConfig.DEBUG) {
                        Log.v("Signup", "Signup Success - username: " + mNewUsername
                                + ", password: " + mNewUserPassword);
                    }
                    startActivity(profileEnterIntent);
                } else {
                    Log.v("Signup", "Signup Fail - username: " + mNewUsername
                            + ", password: " + mNewUserPassword);
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