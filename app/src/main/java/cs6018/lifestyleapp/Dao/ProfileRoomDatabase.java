package cs6018.lifestyleapp.Dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by suchaofan on 10/17/18.
 */


@Database(entities = {ProfileTable.class}, version = 1, exportSchema = false)
public abstract class ProfileRoomDatabase extends RoomDatabase {

    private static volatile ProfileRoomDatabase mInstance;

    public abstract ProfileDao profileDao();

    public static synchronized ProfileRoomDatabase getDatabase(final Context context){
        if(mInstance==null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileRoomDatabase.class, "profile.db").addCallback(sRoomDatabaseCallback).build();
        }
        return mInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(mInstance).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final ProfileDao mDao;

        PopulateDbAsync(ProfileRoomDatabase db){
            mDao = db.profileDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();
//            ProfileTable profileTable = new ProfileTable("dummy_loc","dummy_data");
//            mDao.insert(profileTable);
            return null;
        }
    }
}
