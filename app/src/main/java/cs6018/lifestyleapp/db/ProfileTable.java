package cs6018.lifestyleapp.db;

/**
 * Created by suchaofan on 10/17/18.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "profile_table")
public class ProfileTable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    private String userName;

    @NonNull
    @ColumnInfo(name = "profiledata")
    private String profileJson;

    public ProfileTable(@NonNull String userName, @NonNull String profileJson){
        this.userName = userName;
        this.profileJson = profileJson;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setProfileJson(String profileJson){
        this.profileJson = profileJson;
    }

    public String getUserName(){
        return userName;
    }

    public String getProfileJson(){
        return profileJson;
    }

}
