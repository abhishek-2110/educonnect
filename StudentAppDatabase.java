package com.example.fbauth.Student;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
        StudentAttendanceModel.class,
        StudentAnnouncementModel.class,
        StudentHomeworkModel.class // ✅ Newly added entity
}, version = 3) // 🔥 Bumped version due to schema change
public abstract class StudentAppDatabase extends RoomDatabase {

    private static StudentAppDatabase instance;

    public abstract StudentAttendanceDao studentAttendanceDao();
    public abstract StudentAnnouncementDao studentAnnouncementDao();
    public abstract StudentHomeworkDao studentHomeworkDao(); // ✅ Newly added DAO

    public static synchronized StudentAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StudentAppDatabase.class,
                            "student_attendance_db"
                    )
                    .fallbackToDestructiveMigration() // ✅ Keeps old data safe while upgrading
                    .build();
        }
        return instance;
    }
}
