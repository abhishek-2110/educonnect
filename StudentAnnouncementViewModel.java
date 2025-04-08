package com.example.fbauth.Student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentAnnouncementViewModel extends AndroidViewModel {

    private final LiveData<List<StudentAnnouncementModel>> announcementList;

    public StudentAnnouncementViewModel(@NonNull Application application, String studentSubject) {
        super(application);
        announcementList = StudentAppDatabase.getInstance(application)
                .studentAnnouncementDao()
                .getAnnouncementsBySubject(studentSubject);
    }

    public LiveData<List<StudentAnnouncementModel>> getAnnouncementList() {
        return announcementList;
    }
}
