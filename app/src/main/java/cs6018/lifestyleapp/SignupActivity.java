package cs6018.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs6018.lifestyleapp.Utils.JSONProfileUtils;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    // debug flag
    private boolean debug = true;

    // Define User profile info
    private String mUserName, mAge, mCity, mSex, mNation, mHeight, mWeight, mCurrentPhotoPath;
    private String mTargetWeight, mTargetBMI, mTargetHikes, mTargetCalories, mWeightGoal;

    // Profile JSON data
    private String profileJSon;

    // Define UI view elements
    private EditText etUserName, etAge, etCity, etNation, etHeight, etWeight;
    private EditText etTargetWeight, etTargetBMI, etTargetHikes, etTargetCalories;
    private Spinner spinnerSex, spinnerWeightGoal;
    private Button mButtonCreate;
    private ImageButton mButtonPicture;
    private ImageView mProfilePic; // Profile pic collection parameters and resources.

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ProfileViewModel mProfileViewModel;

    private User mUser = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (debug) {
            setContentView(R.layout.activity_signup_debug);
        }
        else {
            setContentView(R.layout.activity_signup);
        }

        // Find view elements from layout
        etUserName = findViewById(R.id.et_userName);
        etAge = findViewById(R.id.et_age);
        etCity = findViewById(R.id.et_city);
        etNation = findViewById(R.id.et_nation);
        spinnerSex = findViewById(R.id.spinner_sex);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);

        etTargetWeight = findViewById(R.id.et_target_weight);
        etTargetBMI = findViewById(R.id.et_target_bmi);
        etTargetCalories = findViewById(R.id.et_target_calories);
        etTargetHikes = findViewById(R.id.et_target_hikes);
        spinnerWeightGoal = findViewById(R.id.spinner_weight_goal);

        Spinner sexSpinner = (Spinner) findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        sexSpinner.setAdapter(adapter);

        Spinner weightGoalSpinner = (Spinner) findViewById(R.id.spinner_weight_goal);
        ArrayAdapter<CharSequence> wgAdapter = ArrayAdapter.createFromResource(this, R.array.weigth_goal_array, R.layout.spinner_layout);
        wgAdapter.setDropDownViewResource(R.layout.spinner_layout);
        weightGoalSpinner.setAdapter(wgAdapter);

        mButtonCreate = findViewById(R.id.button_get_started);
        mButtonCreate.setOnClickListener(this);

        mButtonPicture = findViewById(R.id.button_get_user_picture);
        mButtonPicture.setOnClickListener(this);

    }

    /** Handles the profile picture getter and user creation buttons.
     *
     * @param view the current view.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_get_user_picture: {
                dispatchTakePictureIntent();
                break;
            }

            case R.id.button_get_started: {

                // Set the user profile
                setUserProfile();

                // Check valid data entered
                if (!isValidData()) {
                    break;
                } else {

                    //Create new Intent Object, and specify class
                    Intent homeActivity = new Intent(this, HomeActivity.class);

                    //Create the view model
                    mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

                    //Set the observer
                    mProfileViewModel.getData().observe(this, nameObserver);

                    loadProfileData(mUserName, profileJSon);

                    // Start the HomeActivity
                    this.startActivity(homeActivity);
                }
            }
        }
    }

    //create an observer that watches the LiveData<User> object
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                System.out.println("profile updated in SignUpActivity!");
            }
        }
    };

    void loadProfileData(String userName, String profileJSon) {
        //pass the user in to the view model
        mProfileViewModel.setUser(userName, profileJSon);
    }

    private boolean setUserProfile() {
        // Set user basic profile data
        mUserName = etUserName.getText().toString();
        mAge = etAge.getText().toString();
        mCity = etCity.getText().toString();
        mNation = etNation.getText().toString();
        mSex = (String) spinnerSex.getSelectedItem();
        mHeight = etHeight.getText().toString();
        mWeight = etWeight.getText().toString();

        // Set user target data
        mTargetWeight = etTargetWeight.getText().toString();
        mTargetBMI = etTargetBMI.getText().toString();
        mTargetCalories = etTargetCalories.getText().toString();
        mTargetHikes = etTargetHikes.getText().toString();
        mWeightGoal = (String) spinnerWeightGoal.getSelectedItem();

        // Check valid data entered
        if (!isValidData()) {
            return false;
        } else {
            mUser.setUserName(mUserName);
            mUser.setAge(mAge);
            mUser.setCity(mCity);
            mUser.setNation(mNation);
            mUser.setSex(mSex);
            mUser.setHeight(mHeight);
            mUser.setWeight(mWeight);
            mUser.setBmi(String.valueOf(CalculatorUtils.computeBMI(mWeight, mHeight)));
            mUser.setBmr(String.valueOf(CalculatorUtils.computeBMR(mWeight, mHeight, mSex, mAge)));
            mUser.setCalories(mUser.getBmr());

            mUser.setTargetWeight(mTargetWeight);
            mUser.setTargetBMI(mTargetBMI);
            mUser.setTargetDailyCalories(mTargetCalories);
            mUser.setTargetHikes(mTargetHikes);
            mUser.setWeightGoal(mWeightGoal);

            // Set start data
            User.setStartData(mUser);

            // Load user info to Json data
            profileJSon = JSONProfileUtils.toProfileJSonData(mUser);
            return true;
        }
    }

    private boolean isValidData() {
        if (mUserName.matches("")
                || mCity.matches("")
                || mAge.matches("")
                || mHeight.matches("")
                || mWeight.matches("")
                || mNation.matches("")
                || mTargetWeight.matches("")
                || mTargetHikes.matches("")
                || mTargetBMI.matches("")
                || mTargetCalories.matches("")) {
            Toast toast = Toast.makeText(SignupActivity.this,
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }

//        if (!mAge.matches("^(0|[1-9][0-9]*)$")
//                || !mHeight.matches("^(0|[1-9][0-9]*)$")
//                || !mWeight.matches("^(0|[1-9][0-9]*)$")
//                || !mTargetHikes.matches("^(0|[1-9][0-9]*)$")
//                || !mTargetBMI.matches("^(0|[1-9][0-9]*)$")
//                || !mTargetCalories.matches("^(0|[1-9][0-9]*)$")) {
//            Toast toast = Toast.makeText(SignupActivity.this,
//                    "Please enter numbers for the height, weight, target hikes, target BMI, and target calories fields", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
//            return false;
//        }
        return true;
    }

    /**
     * Helper function stores the image to a file.
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create the profile image file name.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Captures the image returned from the camera intent, and stores it as the users new profile
     * picture.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            mProfilePic = findViewById(R.id.iv_profile_pic);
            mProfilePic.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }

    /**
     * Stores the picture taken in the intent to a file for future use.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "cs6018.lifestyleapp", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}