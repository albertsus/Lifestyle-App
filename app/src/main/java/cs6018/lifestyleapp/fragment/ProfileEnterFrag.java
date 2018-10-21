package cs6018.lifestyleapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs6018.lifestyleapp.BuildConfig;
import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.activity.HomeActivity;
import cs6018.lifestyleapp.activity.SignupActivity;
import cs6018.lifestyleapp.general.User;
import cs6018.lifestyleapp.utils.CalculatorUtils;
import cs6018.lifestyleapp.utils.JSONProfileUtils;
import cs6018.lifestyleapp.utils.Logger;
import cs6018.lifestyleapp.viewModel.ProfileViewModel;

/**
 * Created by suchaofan on 10/20/18.
 */

public class ProfileEnterFrag extends Fragment
        implements View.OnClickListener{

    // debug flag
    private boolean debug = false;

    // Define User profile info
    private String mUserName, mAge, mCity, mSex, mNation, mHeight, mWeight, mCurrentPhotoPath;
    private String mTargetWeight, mTargetBMI, mTargetHikes, mTargetCalories, mWeightGoal;

    // Profile JSON data
    private String profileJSon;

    // Define UI view elements
    private TextView tvCalcBmi, tvRecBmi;
    private EditText etUserName, etAge, etCity, etHeight, etWeight;
    private EditText etTargetWeight, etTargetBMI, etTargetHikes, etTargetCalories;
    private RadioGroup rgSex, rgWeightGoal;
    private RadioButton rbSex, rbWeightGoal;
    private Button mButtonCreate, mBtnCountry, mBtnBmi;
    private FloatingActionButton mFabtn;
    private ImageButton mButtonPicture;
    private ImageView mProfilePic; // Profile pic collection parameters and resources.
    private ImageView mCoverImg;

    private OnFloatingButtonClickListener mListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private User mUser = User.getInstance();

    public ProfileEnterFrag(){
        // Required empty public constructor
    }

    public interface OnFloatingButtonClickListener {
        public void onFloatingButtonClicked(String param1, String param2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile_enter, container, false);

        try {
            mListener = (OnFloatingButtonClickListener) getContext();
            // Log.d("mContext is ", getContext().toString());
        }catch (ClassCastException ex){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        // Find view elements from layout
        tvCalcBmi = view.findViewById(R.id.tv_calc_bmi);
        tvRecBmi = view.findViewById(R.id.tv_rec_bmi);

        etUserName = view.findViewById(R.id.et_userName);
        etAge = view.findViewById(R.id.et_age);
        etCity = view.findViewById(R.id.et_city);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);

        etTargetWeight = view.findViewById(R.id.et_target_weight);
        etTargetBMI = view.findViewById(R.id.et_target_bmi);
        etTargetCalories = view.findViewById(R.id.et_target_calories);
        etTargetHikes = view.findViewById(R.id.et_target_hikes);

        rgSex = (RadioGroup) view.findViewById(R.id.rg_sex);
        rbSex = (RadioButton) view.findViewById(rgSex.getCheckedRadioButtonId());
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSex = (RadioButton) view.findViewById(checkedId);
            }
        });


        rgWeightGoal = (RadioGroup) view.findViewById(R.id.rg_weight_goal);
        rbWeightGoal = (RadioButton) view.findViewById(rgWeightGoal.getCheckedRadioButtonId());
        rgWeightGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbWeightGoal = (RadioButton) view.findViewById(checkedId);
            }
        });

        mBtnCountry = view.findViewById(R.id.btn_country);
        mBtnCountry.setOnClickListener(this);

        mBtnBmi = view.findViewById(R.id.btn_bmi);
        mBtnBmi.setOnClickListener(this);

        mButtonCreate = view.findViewById(R.id.button_get_started);
        mButtonCreate.setOnClickListener(this);

        mButtonPicture = view.findViewById(R.id.button_get_user_picture);
        mButtonPicture.setOnClickListener(this);

        mFabtn = (FloatingActionButton) getActivity().findViewById(R.id.next);
        mFabtn.setOnClickListener(this);

        mCoverImg = (ImageView) getActivity().findViewById(R.id.cover_img);
        mCoverImg.setImageResource(R.drawable.run_img);

        return view;
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

            case R.id.btn_country: {
                selectCountry();
                break;
            }

            case R.id.btn_bmi: {
                recommendBMI();
                break;
            }

            case R.id.next: {
                // Check valid data entered
                if (isValidData()) {
                    // Set the user profile
                    setUserProfile();
                    mListener.onFloatingButtonClicked(mUserName, profileJSon);
                }
                break;
            }

            case R.id.button_get_started: {
                // Check valid data entered
                if (isValidData()) {
                    // Set the user profile
                    setUserProfile();
                    mListener.onFloatingButtonClicked(mUserName, profileJSon);
                }
                break;
            }

        }
    }

    private void setUserProfile() {
        mUser.setUserName(mUserName);
        mUser.setAge(mAge);
        mUser.setCity(mCity);
        mUser.setNation(mNation);
        mUser.setSex(mSex);
        mUser.setHeight(mHeight);
        mUser.setWeight(mWeight);
        mUser.setBmi(CalculatorUtils.computeBMI(mWeight, mHeight).toString());
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
    }

    private boolean isValidData() {
        if ((mUserName = etUserName.getText().toString()).matches("")
                || (mCity = etCity.getText().toString()).matches("")
                || (mAge = etAge.getText().toString()).matches("")
                || (mHeight = etHeight.getText().toString()).matches("")
                || (mNation.matches(""))
                || (mWeight = etWeight.getText().toString()).matches("")
                || (rbSex == null)
                || (mSex = rbSex.getText().toString()).matches("")
                || (mTargetWeight = etTargetWeight.getText().toString()).matches("")
                || (mTargetHikes = etTargetHikes.getText().toString()).matches("")
                || (mTargetBMI = etTargetBMI.getText().toString()).matches("")
                || (mTargetCalories = etTargetCalories.getText().toString()).matches("")
                || (rbWeightGoal == null)
                || (mWeightGoal = rbWeightGoal.getText().toString()).matches("")) {
            if (BuildConfig.DEBUG) {
                Logger.log("Detect Invalid Profile Data");
            }
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }

        if (!mAge.matches("^(0|[1-9][0-9]*)$")
                || !mHeight.matches("^(0|[1-9][0-9]*)$")
                || !mWeight.matches("^(0|[1-9][0-9]*)$")
                || !mTargetHikes.matches("^(0|[1-9][0-9]*)$")
                || !mTargetBMI.matches("^(0|[1-9][0-9]*)$")
                || !mTargetCalories.matches("^(0|[1-9][0-9]*)$")) {
            if (BuildConfig.DEBUG) {
                Logger.log("Detect Invalid Digit");
            }
            Toast toast = Toast.makeText(getActivity(),
                    "Please enter numbers for the height, weight, target hikes, target BMI, and target calories fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    private void selectCountry() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                mNation = name;
                mBtnCountry.setBackgroundResource(flagDrawableResID);
                // Toast.makeText(getActivity(), mNation, Toast.LENGTH_SHORT).show();
                picker.dismiss();
            }
        });
    }

    private void recommendBMI() {
        if (etHeight.getText().toString() != "" && etWeight.getText().toString() != "") {
            Float bmi = CalculatorUtils.computeBMI(etWeight.getText().toString(), etHeight.getText().toString());
            tvCalcBmi.setText("BMI: " + bmi.toString());
            if (bmi < 18.5) {
                tvRecBmi.setText("Under Weight");
            } else if (bmi > 18.5 && bmi < 24.99) {
                tvRecBmi.setText("Normal Weight");
            } else if (bmi > 25 && bmi < 29.99) {
                tvRecBmi.setText("Over Weight");
            } else {
                tvRecBmi.setText("Obesity");
            }
        }
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
            mProfilePic = getActivity().findViewById(R.id.iv_profile_pic);
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
