package cs6018.lifestyleapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 * Created by suchaofan on 10/13/18.
 */

public class ProfileViewModel extends AndroidViewModel {
    private MutableLiveData<User> jsonData;
    private ProfileRepository mProfileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mProfileRepository = new ProfileRepository(application);
        jsonData = mProfileRepository.getData();
    }

    public void setUser(String userName, String userJson) {
        mProfileRepository.setUser(userName, userJson);
    }

    public LiveData<User> getData() { return jsonData ; }
}
