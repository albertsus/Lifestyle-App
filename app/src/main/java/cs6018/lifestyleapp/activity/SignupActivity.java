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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;

public class SignupActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private String mNewUsername, mNewUserPassword;
    private CardView mCvLogin, mCvSignup;

    private FirebaseAuth mAuth;

    private DatabaseReference mDbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        setContentView(R.layout.activity_signup);

        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);

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
                    startActivity(profileEnterIntent);
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid Email/Password Entered",
                            Toast.LENGTH_SHORT).show();
                }
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("uuid", user.getUid());
                            User.setUUID(user.getUid());
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
                            Log.d("SignInSucess", user.getEmail());
                            Log.d("SignInSucess", user.getUid());

                            // Check if UUID exists in database, jump to HomeActivity directly
                            checkUserInDB(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean UserMatch(String username, String password) {
        return true;
    }

    private void checkUserInDB(final String uuid) {
        mDbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uuid)) {
                    startActivity(new Intent(SignupActivity.this, ProfileEnterActivity.class));
                } else {
                    Log.v("UserNotInDB", uuid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    }

}