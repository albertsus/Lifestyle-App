package cs6018.lifestyleapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mTvWind;
    private TextView mTvUVIndex;

    private WeatherViewModel mWeatherViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation;

    private String testLocationString = "lat=40.759926&lon=-111.884888";

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

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            mFusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            System.out.println("location: " + location);
//                            if (location != null) {
//                                mLocation = location;
//                                mWeatherViewModel.setLocation(Double.toString(mLocation.getLatitude()) + ", " + Double.toString(mLocation.getLongitude()));
//                            }
//                            // hard coded default for testing api and view update
//                            else {
//                                mWeatherViewModel.setLocation(testLocationString);
//                            }
//                        }
//                    });
//            return;
//        }
        // hard coded default for testing api and view update
        mWeatherViewModel.setLocation(testLocationString);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // loadWeatherData(mLocation);
                loadWeatherData(testLocationString);
            }
        });
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> nameObserver  = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            // Update the UI if this data variable changes
            if(weatherData!=null) {
                mTvTempCurrent.setText("" + Math.round(weatherData.getTemperature().getTemp() - 273.15) + "\u00B0");
                mTvTempHigh.setText("" + Math.round(weatherData.getTemperature().getMaxTemp()  - 273.15) + "\u00B0");
                mTvTempLow.setText("" + Math.round(weatherData.getTemperature().getMinTemp() - 273.15) + "\u00B0");
                mTvHumidity.setText("" + weatherData.getCurrentCondition().getHumidity() + "%");
                mTvWeatherSummary.setText("" + weatherData.getCurrentCondition().getDescr());
                mTvLocation.setText("" + weatherData.getLocationData().getCity());
            }
        }
    };

    void loadWeatherData(String location){
        //pass the location in to the view model
        //mWeatherViewModel.setLocation(Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));
        // hard coded default for testing api and view update
        mWeatherViewModel.setLocation(testLocationString);
    }
}
