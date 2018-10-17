package cs6018.lifestyleapp;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;

import cs6018.lifestyleapp.ProfileDao;
import cs6018.lifestyleapp.ProfileRoomDatabase;
import cs6018.lifestyleapp.Utils.JSONProfileUtils;
import cs6018.lifestyleapp.ProfileTable;
import cs6018.lifestyleapp.User;

/**
 * Created by suchaofan on 10/13/18.
 */

public class ProfileRepository {
    private final MutableLiveData<User> jsonData =
            new MutableLiveData<User>();
    private String mUserJson;
    private ProfileDao mProfileDao;
    private String mJsonString;
    private String mUsername;

    public ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        // loadData();
    }
    public void setUser(String userName, String user) {
        mUsername = userName;
        mUserJson = user;
        loadData();
    }

    public MutableLiveData<User> getData() {
        return jsonData;
    }

    private void loadData(){
        new AsyncTask<String,Void,String>() {
            @Override
            protected String doInBackground(String... strings) {
                String userJson = strings[0];
                return userJson;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    mJsonString = s;
                    insert();
                }
                try {
                    jsonData.setValue(JSONProfileUtils.getProfileData(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(mUserJson);
    }

    private void insert(){
        ProfileTable profileTable = new ProfileTable(mUsername ,mJsonString);
        new insertAsyncTask(mProfileDao).execute(profileTable);
    }

    private static class insertAsyncTask extends AsyncTask<ProfileTable,Void,Void>{
        private ProfileDao mAsyncTaskDao;

        insertAsyncTask(ProfileDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ProfileTable... profileTables) {
            mAsyncTaskDao.insert(profileTables[0]);
            return null;
        }
    }
}

