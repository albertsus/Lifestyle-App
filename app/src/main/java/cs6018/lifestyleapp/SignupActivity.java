package cs6018.lifestyleapp;

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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    // Profile pic collection parameters and resources.
    ImageView mProfilePic;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Input strings from form.
    private String mUserName, mAge, mCity, mSex, mNation, mHeight, mWeight, mCurrentPhotoPath;

    private String mTargetWeight, mTargetBMI, mTargetHikes, mTargetCalories, mWeightGoal;

    // UI elements.
    Button mButtonCreate;
    ImageButton mButtonPicture;
    EditText etUserName, etAge, etCity, etNation, etHeight, etWeight;
    Spinner spinnerSex;

    EditText etTargetWeight, etTargetBMI, etTargetHikes, etTargetCalories;
    Spinner spinnerWeightGoal;

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
                // Get the user data from the input fields.
                etUserName = findViewById(R.id.et_userName);
                mUserName = etUserName.getText().toString();

                etAge = findViewById(R.id.et_age);
                mAge = etAge.getText().toString();

                etCity = findViewById(R.id.et_city);
                mCity = etCity.getText().toString();

                etNation = findViewById(R.id.et_nation);
                mNation = etNation.getText().toString();

                spinnerSex = findViewById(R.id.spinner_sex);
                mSex = (String) spinnerSex.getSelectedItem();

                etHeight = findViewById(R.id.et_height);
                mHeight = etHeight.getText().toString();

                etWeight = findViewById(R.id.et_weight);
                mWeight = etWeight.getText().toString();

                etTargetWeight = findViewById(R.id.et_target_weight);
                mTargetWeight = etTargetWeight.getText().toString();

                etTargetBMI = findViewById(R.id.et_target_bmi);
                mTargetBMI = etTargetBMI.getText().toString();

                etTargetCalories = findViewById(R.id.et_target_calories);
                mTargetCalories = etTargetCalories.getText().toString();

                etTargetHikes = findViewById(R.id.et_target_hikes);
                mTargetHikes = etTargetHikes.getText().toString();

                spinnerWeightGoal = findViewById(R.id.spinner_weight_goal);
                mWeightGoal = (String) spinnerWeightGoal.getSelectedItem();

                // Handle empty submissions.
                if (mUserName.matches("") || mCity.matches("")
                        || mAge.matches("") || mHeight.matches("")
                        || mWeight.matches("") || mNation.matches("")) {
                    Toast toast = Toast.makeText(SignupActivity.this,
                            "Please enter all fields", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    break;
                }
                else {
                    //Create new Intent Object, and specify class
                    Intent homeActivity = new Intent(this, HomeActivity.class);
                    //Set data using putExtra method which take any key and value which we want to send
                    homeActivity.putExtra("USERNAME", mUserName);
                    homeActivity.putExtra("AGE", mAge);
                    homeActivity.putExtra("SEX", mSex);
                    homeActivity.putExtra("HEIGHT", mHeight);
                    homeActivity.putExtra("WEIGHT", mWeight);
                    homeActivity.putExtra("NATION", mNation);
                    homeActivity.putExtra("CITY", mCity);
                    homeActivity.putExtra("PROFILE_PIC", mCurrentPhotoPath);

                    homeActivity.putExtra("TARGET_WEIGHT", mTargetWeight);
                    homeActivity.putExtra("TARGET_BMI", mTargetBMI);
                    homeActivity.putExtra("TARGET_CALORIES", mTargetCalories);
                    homeActivity.putExtra("TARGET_HIKES", mTargetHikes);
                    homeActivity.putExtra("WEIGHT_GOAL", mWeightGoal);

                    this.startActivity(homeActivity);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Spinner sexSpinner = (Spinner) findViewById(R.id.spinner_sex);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        sexSpinner.setAdapter(adapter);


        Spinner weightGoalSpinner = (Spinner) findViewById(R.id.spinner_weight_goal);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> wgAdapter = ArrayAdapter.createFromResource(this, R.array.weigth_goal_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        wgAdapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        weightGoalSpinner.setAdapter(wgAdapter);

        mButtonCreate = findViewById(R.id.button_get_started);
        mButtonCreate.setOnClickListener(this);

        mButtonPicture = findViewById(R.id.button_get_user_picture);
        mButtonPicture.setOnClickListener(this);

    }
}
