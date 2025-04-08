package com.example.fbauth.Student;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentAnnouncementDao {

    @Insert
    void insert(StudentAnnouncementModel model);

    @Insert
    void insertAll(List<StudentAnnouncementModel> announcements);

    @Query("SELECT * FROM student_announcement_table WHERE subject = :subject ORDER BY timestamp DESC")
    LiveData<List<StudentAnnouncementModel>> getAnnouncementsBySubject(String subject);

    @Query("DELETE FROM student_announcement_table")
    void clearAll();
}
