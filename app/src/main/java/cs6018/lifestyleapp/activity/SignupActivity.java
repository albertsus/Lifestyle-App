package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cs6018.lifestyleapp.BuildConfig;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.utils.Logger;

public class SignupActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private String mNewUsername, mNewUserPassword;
    private CardView mCvLogin, mCvSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                        if (isValidEmail(mUn) && isValidPassword(mPwd)) {
                            signIn(mUn, mPwd);
                        }
//                        if (UserMatch(mUn, mPwd)) { // TODO: Check if username:password in db
//                            if (BuildConfig.DEBUG) {
//                                Log.v("Login", "Login Success - username: " + mUn
//                                        + ", password: " + mPwd);
//                            }
//                            dialog.dismiss();
//                            startActivity(profileEnterIntent);
//                        } else {
//                            if (BuildConfig.DEBUG) {
//                                Log.v("Login", "Login Fail - username: " + mUn
//                                        + ", password: " + mPwd);
//                            }
//                            Toast.makeText(getApplication(), "Invalid Username/Password, please try again!", Toast.LENGTH_SHORT).show();
//                            //startActivity(profileEnterIntent);
//                        }
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
                if(isValidEmail(mNewUsername) && isValidPassword(mNewUserPassword)) {
                    register(mNewUsername, mNewUserPassword);
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid Email/Password Entered",
                            Toast.LENGTH_SHORT).show();
                }
//                if (!UserInDB(mNewUsername)) {
//                    addToDB(mNewUsername, mNewUserPassword); // TODO: add username:password to db
//                    if (BuildConfig.DEBUG) {
//                        Log.v("Signup", "Signup Success - username: " + mNewUsername
//                                + ", password: " + mNewUserPassword);
//                    }
//                    startActivity(profileEnterIntent);
//                } else {
//                    Log.w("Signup", "Signup Fail - username: " + mNewUsername
//                            + ", password: " + mNewUserPassword);
//                    Toast.makeText(getApplication(), "Username already existed, please try a new name!", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            Log.d("SignInSucess", user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    private boolean UserMatch(String username, String password) {
        return true;
    }

    private boolean UserInDB(String username) {
        return true;
    }

    private void addInfoToDB(String username, String password) {

    }

    // Validate email address for new accounts.
    private boolean isValidEmail(String email) {
        boolean valid = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!valid) {
            mEtUsername.setError("Invalid Email: " + email);
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String pwd) {
        boolean valid = pwd != null && !pwd.isEmpty();
        if (!valid) {
            mEtPassword.setError("Invalid Password: " + pwd);
            return false;
        }
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Signout", currentUser.getEmail());
            mAuth.signOut();
        }
        // updateUI(currentUser);
    }

}