package cs6018.lifestyleapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cs6018.lifestyleapp.ProfileTable;

/**
 * Created by suchaofan on 10/17/18.
 */

@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProfileTable profileTable);

    @Query("DELETE FROM profile_table")
    void deleteAll();

    @Query("SELECT * from profile_table ORDER BY username ASC")
    LiveData<List<ProfileTable>>  getAll();

}
