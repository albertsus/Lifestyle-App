package cs6018.lifestyleapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WeatherActivity extends AppCompatActivity {

    private TextView mTvLocation;
    private TextView mTvWeatherSummary;
    private TextView mTvTempHigh;
    private TextView mTvTempLow;
    private TextView mTvTempCurrent;
    private TextView mTvHumidity;

    private TextView mTvWind;
    private TextView mTvUVIndex;

    private WeatherViewModel mWeatherViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mTvWeatherSummary = (TextView) findViewById(R.id.tv_weather_summary);

        mTvTempHigh = (TextView) findViewById(R.id.tv_temp_high);
        mTvTempLow = (TextView) findViewById(R.id.tv_temp_low);
        mTvTempCurrent = (TextView) findViewById(R.id.tv_temp_current);

        mTvHumidity = (TextView) findViewById(R.id.tv_humidity);
        mTvWind = (TextView) findViewById(R.id.tv_wind);
        mTvUVIndex = (TextView) findViewById(R.id.tv_uvindex);

        //Create the view model
        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        //Set the observer
        mWeatherViewModel.getData().observe(this, nameObserver);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                               mLocation = location;
                            }
                        }
                    });
            return;
        }
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> nameObserver  = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            // Update the UI if this data variable changes
            if(weatherData!=null) {
                mTvTempCurrent.setText("" + Math.round(weatherData.getTemperature().getTemp() - 273.15) + " C");
                mTvHumidity.setText("" + weatherData.getCurrentCondition().getHumidity() + "%");
                mTvWeatherSummary.setText("" + weatherData.getCurrentCondition().getDescr());
            }
        }
    };

    void loadWeatherData(String location){
        //pass the location in to the view model
        mWeatherViewModel.setLocation(location);
    }
}
