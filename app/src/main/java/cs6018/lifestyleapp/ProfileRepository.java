package cs6018.lifestyleapp;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by suchaofan on 10/13/18.
 */

public class ProfileRepository {
    private final MutableLiveData<User> userData =
            new MutableLiveData<User>();
    private User mUser;

    public void setUser(User user) {
        mUser = user;
        loadData();
    }

    public MutableLiveData<User> getData() {
        return userData;
    }

    private void loadData(){
        new AsyncTask<User,Void,User>() {
            @Override
            protected User doInBackground(User... strings) {
                return strings[0];
            }

            @Override
            protected void onPostExecute(User user) {
                try {
                    userData.setValue(user);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }.execute(mUser);
    }
}

