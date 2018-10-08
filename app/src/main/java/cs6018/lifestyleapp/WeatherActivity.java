package cs6018.lifestyleapp;

import android.support.v7.app.AppCompatActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

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

    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> nameObserver  = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            // Update the UI if this data variable changes
            if(weatherData!=null) {
                mTvTempCurrent.setText("" + Math.round(weatherData.getTemperature().getTemp() - 273.15) + " C");
                mTvHumidity.setText("" + weatherData.getCurrentCondition().getHumidity() + "%");
                //mTv.setText("" + weatherData.getCurrentCondition().getPressure() + " hPa");
            }
        }
    };


}
