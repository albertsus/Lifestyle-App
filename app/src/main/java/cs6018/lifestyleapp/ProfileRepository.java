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
        System.out.println("mUser in setUser: " + mUser);
        loadData();
    }

    public MutableLiveData<User> getData() {
        return userData;
    }

    private void loadData(){
        new AsyncTask<User,Void,User>() {
            @Override
            protected User doInBackground(User... strings) {
                User user = strings[0];
                System.out.println("user in async: " + user);
                // User retrievedJsonData = null;
//                if (user != null) {
//                    try {
//                        retrievedJsonData = mUser;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                return user;
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

