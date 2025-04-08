package com.example.fbauth.Student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StudentAnnouncementViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String subject;

    public StudentAnnouncementViewModelFactory(Application application, String subject) {
        this.application = application;
        this.subject = subject;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StudentAnnouncementViewModel(application, subject);
    }
}
