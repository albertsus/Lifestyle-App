package cs6018.lifestyleapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs6018.lifestyleapp.R;
import cs6018.lifestyleapp.general.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment implements View.OnClickListener {

    private TextView mTvUserName,  mTvAge, mTvSex, mTvCity, mTvNation, mTvHeight, mTvWeight;

    private Button mBtEdit;

    private DatabaseReference mDbUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUUID());

        mTvUserName = (TextView) view.findViewById(R.id.tv_userName_data);
        mTvAge = (TextView) view.findViewById(R.id.tv_age_data);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex_data);
        mTvCity = (TextView) view.findViewById(R.id.tv_city_data);
        mTvNation = (TextView) view.findViewById(R.id.tv_nation_data);
        mTvHeight = (TextView) view.findViewById(R.id.tv_height_data);
        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_data);

        mBtEdit = (Button) view.findViewById(R.id.bt_edit_profile);
        mBtEdit.setOnClickListener(this);

        fillProfileInfo();

        return view;
    }

    private void fillProfileInfo() {
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTvUserName.setText(dataSnapshot.child("userName").getValue(String.class));
                mTvAge.setText(dataSnapshot.child("age").getValue(String.class));
                mTvSex.setText(dataSnapshot.child("sex").getValue(String.class));
                mTvNation.setText(dataSnapshot.child("nation").getValue(String.class));
                mTvCity.setText(dataSnapshot.child("city").getValue(String.class));
                mTvHeight.setText(dataSnapshot.child("height").getValue(String.class));
                mTvWeight.setText(dataSnapshot.child("weight").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        // Create new fragment and transaction
        if (isValidData()) {

            Fragment editProfileFrag = new EditProfileFrag();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.main_container, editProfileFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    private boolean isValidData() {
        if (mTvUserName.getText().toString().matches("")
                || mTvCity.getText().toString().matches("")
                || mTvAge.getText().toString().matches("")
                || mTvHeight.getText().toString().matches("")
                || mTvWeight.getText().toString().matches("")
                || mTvNation.getText().toString().matches("")) {
            Toast toast = Toast.makeText(getActivity(),
                    "Invalid data entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }

//        if (!mTvAge.getText().toString().matches("^(0|[1-9][0-9]*)$")
//                || !mTvHeight.getText().toString().matches("^(0|[1-9][0-9]*)$")
//                || !mTvWeight.getText().toString().matches("^(0|[1-9][0-9]*)$")) {
//            Toast toast = Toast.makeText(getActivity(),
//                    "Please enter numbers for the height, weight, target hikes, target BMI, and target calories fields", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
//            return false;
//        }
        return true;
    }
}
