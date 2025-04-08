package com.example.fbauth.Student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentHomeworkViewModel extends AndroidViewModel {

    private final LiveData<List<StudentHomeworkModel>> homeworkList;

    public StudentHomeworkViewModel(@NonNull Application application, String studentId) {
        super(application);
        homeworkList = StudentAppDatabase.getInstance(application)
                .studentHomeworkDao()
                .getHomeworkByStudentId(studentId);
    }

    public LiveData<List<StudentHomeworkModel>> getHomeworkList() {
        return homeworkList;
    }
}
