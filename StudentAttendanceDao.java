package com.example.fbauth.Student;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentAttendanceDao {

    @Insert
    void insert(StudentAttendanceModel model); // ✅ Fix: added @Insert here

    @Insert
    void insertAll(List<StudentAttendanceModel> models);

    @Query("SELECT * FROM attendance_table WHERE studentId = :studentId")
    LiveData<List<StudentAttendanceModel>> getAttendanceForStudent(String studentId);

    @Query("DELETE FROM attendance_table") // ✅ Optional: reset local cache
    void clearAll();
}
