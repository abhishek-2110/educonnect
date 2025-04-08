package com.example.fbauth.Student;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentHomeworkDao {

    @Insert
    void insert(StudentHomeworkModel model);

    @Insert
    void insertAll(List<StudentHomeworkModel> homeworkList);

    @Query("SELECT * FROM student_homework_table WHERE studentId = :studentId ORDER BY timestamp DESC")
    LiveData<List<StudentHomeworkModel>> getHomeworkByStudentId(String studentId);

    @Query("DELETE FROM student_homework_table")
    void clearAll();
}
