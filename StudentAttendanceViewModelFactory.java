package com.example.fbauth.Student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StudentAttendanceViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String studentId;

    public StudentAttendanceViewModelFactory(Application application, String studentId) {
        this.application = application;
        this.studentId = studentId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StudentAttendanceViewModel(application, studentId);
    }
}
