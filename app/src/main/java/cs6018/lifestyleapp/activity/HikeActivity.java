package cs6018.lifestyleapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs6018.lifestyleapp.R;

public class HikeActivity extends AppCompatActivity {

    private String mSearchString = "Hiking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        Uri searchUri = Uri.parse("geo:40.767778,-111.845205?q=" + mSearchString);

        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
