package com.example.fbauth.Student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentAttendanceViewModel extends AndroidViewModel {

    private final LiveData<List<StudentAttendanceModel>> attendanceList;

    public StudentAttendanceViewModel(@NonNull Application application, String studentId) {
        super(application);
        attendanceList = StudentAppDatabase.getInstance(application)
                .studentAttendanceDao()
                .getAttendanceForStudent(studentId);
    }

    public LiveData<List<StudentAttendanceModel>> getAttendanceList() {
        return attendanceList;
    }
}
