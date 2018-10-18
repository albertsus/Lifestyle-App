package cs6018.lifestyleapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import cs6018.lifestyleapp.general.User;

/**
 * Created by suchaofan on 10/17/18.
 */

public class JSONProfileUtils {

    public static String toProfileJSonData(User user) {
        JSONObject oj = new JSONObject();
        try {
            oj.put("username", user.getUserName());
            oj.put("sex", user.getSex());
            oj.put("age", user.getAge());
            oj.put("city", user.getCity());
            oj.put("nation", user.getNation());
            oj.put("height", user.getHeight());
            oj.put("weight", user.getWeight());
            oj.put("bmi", user.getBmi());
            oj.put("bmr", user.getBmr());
            oj.put("calories", user.getCalories());

            oj.put("target_weight", user.getTargetWeight());
            oj.put("target_bmi", user.getTargetBMI());
            oj.put("target_calories", user.getTargetDailyCalories());
            oj.put("target_hikes", user.getTargetHikes());
            oj.put("weight_goal", user.getWeightGoal());

            return oj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getProfileData(String data) throws JSONException{
        User userData = new User();

        //Start parsing JSON data
        JSONObject jObj = new JSONObject(data); //Must throw JSONException

        // Get User Profile Data
        userData.setUserName(jObj.getString("username"));
        userData.setSex(jObj.getString("sex"));
        userData.setAge(jObj.getString("age"));
        userData.setCity(jObj.getString("city"));
        userData.setNation(jObj.getString("nation"));
        userData.setHeight(jObj.getString("height"));
        userData.setWeight(jObj.getString("weight"));
        userData.setBmi(jObj.getString("bmi"));
        userData.setBmr(jObj.getString("bmr"));
        userData.setCalories(jObj.getString("calories"));

        // Get User Target Data
        userData.setTargetWeight(jObj.getString("target_weight"));
        userData.setTargetBMI(jObj.getString("target_bmi"));
        userData.setTargetDailyCalories(jObj.getString("target_calories"));
        userData.setTargetHikes(jObj.getString("target_hikes"));
        userData.setWeightGoal(jObj.getString("weight_goal"));

        return userData;
    }
}
