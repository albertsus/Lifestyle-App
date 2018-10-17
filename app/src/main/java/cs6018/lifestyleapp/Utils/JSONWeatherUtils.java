package cs6018.lifestyleapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cs6018.lifestyleapp.LocationData;
import cs6018.lifestyleapp.WeatherData;

//Declare methods as static. We don't want to create objects of this class.
public class JSONWeatherUtils {
    public static WeatherData getWeatherData(String data) throws JSONException{
        WeatherData weatherData = new WeatherData();

        //Start parsing JSON data
        JSONObject jsonObject = new JSONObject(data); //Must throw JSONException

        //Get Current Condition
        WeatherData.CurrentCondition currentCondition = weatherData.getCurrentCondition();
        JSONObject jsonMain = jsonObject.getJSONObject("main");
        currentCondition.setHumidity(jsonMain.getInt("humidity"));
        currentCondition.setPressure(jsonMain.getInt("pressure"));

        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        JSONObject jsonWeather = jsonArray.getJSONObject(0);
        currentCondition.setWeatherId(jsonWeather.getLong("id"));
        currentCondition.setCondition(jsonWeather.getString("main"));
        currentCondition.setDescr(jsonWeather.getString("description"));
        currentCondition.setIcon(jsonWeather.getString("icon"));

        weatherData.setCurrentCondition(currentCondition);

        //Get the temperature, wind and cloud data
        WeatherData.Temperature temperature = weatherData.getTemperature();
        temperature.setMaxTemp(jsonMain.getDouble("temp_max"));
        temperature.setMinTemp(jsonMain.getDouble("temp_min"));
        temperature.setTemp(jsonMain.getDouble("temp"));
        weatherData.setTemperature(temperature);

        JSONObject jsonWind = jsonObject.getJSONObject("wind");
        WeatherData.Wind wind = weatherData.getWind();
        wind.setSpeed(jsonWind.getDouble("speed"));
        // wind.setDeg(jsonWind.getDouble("deg"));

        JSONObject jsonClouds = jsonObject.getJSONObject("clouds");
        WeatherData.Clouds clouds = weatherData.getClouds();
        clouds.setPerc(jsonClouds.getLong("all"));

        //Get the LocationData
        LocationData locationData = new LocationData();
        JSONObject jsonCoord = jsonObject.getJSONObject("coord");
        locationData.setLongitude(jsonCoord.getDouble("lon"));
        locationData.setLatitude(jsonCoord.getDouble("lat"));

        JSONObject jsonSys = jsonObject.getJSONObject("sys");
        locationData.setCountry(jsonSys.getString("country"));
        locationData.setSunrise(jsonSys.getLong("sunrise"));
        locationData.setSunset(jsonSys.getLong("sunset"));

        String city = jsonObject.getString("name");
        locationData.setCity(city);

        weatherData.setLocationData(locationData);

        return weatherData;
    }
}
