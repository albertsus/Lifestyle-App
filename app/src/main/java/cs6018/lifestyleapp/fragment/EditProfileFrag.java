package cs6018.lifestyleapp.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.CalculatorUtils;

/**
 * Created by suchaofan on 9/28/18.
 */

public class EditProfileFrag extends Fragment implements View.OnClickListener {

    private String mUserName, mAge, mSex, mCity, mNation, mHeight, mWeight, mCurrentPhotoPath;

    private EditText mEtUserName, mEtAge, mEtCity, mEtNation, mEtHeight, mEtWeight;
    private Spinner sexSpinner;
    private Button mBtUpdate;
    private ImageButton mBtPicture;
    private ImageView mProfilePic;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private DatabaseReference mDbUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());

        // Find view elements from layout
        mEtUserName = (EditText) view.findViewById(R.id.et_userName);
        mEtAge = (EditText) view.findViewById(R.id.et_age);
        mEtCity = (EditText) view.findViewById(R.id.et_city);
        mEtNation = (EditText) view.findViewById(R.id.et_nation);
        mEtHeight = (EditText) view.findViewById(R.id.et_height);
        mEtWeight = (EditText) view.findViewById(R.id.et_weight);

        mBtUpdate = (Button) view.findViewById(R.id.bt_update_profile);
        mBtUpdate.setOnClickListener(this);

        mBtPicture = (ImageButton) view.findViewById(R.id.bt_edit_user_picture);
        mBtPicture.setOnClickListener(this);

        sexSpinner = (Spinner) view.findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sex_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        sexSpinner.setAdapter(adapter);

        // Init the profile data received from ProfileFrag
        InitProfileData();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_edit_user_picture: {
                dispatchTakePictureIntent();
                break;
            }

            case R.id.bt_update_profile: {
                // Check valid data entered
                if (!isValidData()) {
                    break;
                } else {
                    // Show updated success info
                    Toast toast = Toast.makeText(getActivity(),
                            "Updated Profile Success!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();

                    // Update User Profile
                    updateProfile();

                    // Route to ProfileFrag
                    getFragmentManager().popBackStackImmediate();
                }
            }
        }
    }

    /**
     * Init the profile data when first in the fragment
     */
    private void InitProfileData() {
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserName = dataSnapshot.child("userName").getValue(String.class);
                mAge = dataSnapshot.child("age").getValue(String.class);
                mSex = dataSnapshot.child("age").getValue(String.class);
                mNation = dataSnapshot.child("nation").getValue(String.class);
                mCity = dataSnapshot.child("city").getValue(String.class);
                mHeight = dataSnapshot.child("height").getValue(String.class);
                mWeight = dataSnapshot.child("weight").getValue(String.class);

                // Set the profile data
                mEtUserName.setText(mUserName);
                mEtAge.setText(mAge);
                mEtHeight.setText(mHeight);
                mEtWeight.setText(mWeight);
                mEtNation.setText(mNation);
                mEtCity.setText(mCity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Update the profile once click 'Edit' Button
     */
    private void updateProfile() {
        mDbUsers.child("userName").setValue(mEtUserName.getText().toString());
        mDbUsers.child("age").setValue(mEtAge.getText().toString());
        mDbUsers.child("sex").setValue(sexSpinner.getSelectedItem().toString());
        mDbUsers.child("nation").setValue(mEtNation.getText().toString());
        mDbUsers.child("city").setValue(mEtCity.getText().toString());
        mDbUsers.child("height").setValue(mEtHeight.getText().toString());
        mDbUsers.child("weight").setValue(mEtWeight.getText().toString());

        String calories = String.valueOf(CalculatorUtils.computeBMR(mWeight, mHeight, mSex, mAge));
        mDbUsers.child("calories").setValue(calories);
        //mUser.setProfilePic(mCurrentPhotoPath);
    }

    private boolean isValidData() {
        if (mUserName.matches("") || mCity.matches("")
                || mAge.matches("") || mHeight.matches("")
                || mWeight.matches("") || mNation.matches("")) {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
            mProfilePic = getView().findViewById(R.id.iv_profile_pic);
            mProfilePic.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }

    /**
     * Stores the picture taken in the intent to a file for future use.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "cs6018.lifestyleapp", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}
