package com.example.fbauth.HomeFiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HomeModel.class}, version = 1)
public abstract class HomeAppDatabase extends RoomDatabase {

    private static HomeAppDatabase instance;

    public abstract HomeDao homeDao();

    public static synchronized HomeAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            HomeAppDatabase.class, "home_app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
