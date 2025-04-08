package com.example.fbauth.TeacherFiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TeacherMessageModel.class}, version = 2)
public abstract class TeacherAppDatabase extends RoomDatabase {

    private static TeacherAppDatabase instance;

    public abstract TeacherMessageDao teacherMessageDao();

    public static synchronized TeacherAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TeacherAppDatabase.class, "teacher_app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
