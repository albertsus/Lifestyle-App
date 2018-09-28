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
    String mCurrentPhotoPath;

    // Input strings from form.
    private String mUserName, mAge, mCity, mSex, mNation, mHeight, mWeight;

    // UI elements.
    Button mButtonCreate;
    ImageButton mButtonPicture;
    EditText etUserName, etAge, etCity, etNation, etHeight, etWeight;
    Spinner spinnerSex;

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
                    this.startActivity(homeActivity);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_sex);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mButtonCreate = findViewById(R.id.button_get_started);
        mButtonCreate.setOnClickListener(this);

        mButtonPicture = findViewById(R.id.button_get_user_picture);
        mButtonPicture.setOnClickListener(this);

    }
}
