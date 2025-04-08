package com.example.fbauth.TeacherFiles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TeacherMessageViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String teacherId;

    public TeacherMessageViewModelFactory(Application application, String teacherId) {
        this.application = application;
        this.teacherId = teacherId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TeacherMessageViewModel(application, teacherId);
    }
}
