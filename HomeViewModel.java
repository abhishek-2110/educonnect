package com.example.fbauth.HomeFiles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class HomeViewModel extends AndroidViewModel {

    private final HomeDao homeDao;
    private final LiveData<HomeModel> homeInfoLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        HomeAppDatabase db = HomeAppDatabase.getInstance(application);
        homeDao = db.homeDao();
        homeInfoLiveData = homeDao.getHomeInfo();
        fetchFromFirestore();
    }

    public LiveData<HomeModel> getHomeInfo() {
        return homeInfoLiveData;
    }

    private void fetchFromFirestore() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(snapshot -> {
                    int studentCount = 0, teacherCount = 0, parentCount = 0;
                    for (QueryDocumentSnapshot doc : snapshot) {
                        String role = doc.getString("role");
                        if ("Student".equalsIgnoreCase(role)) studentCount++;
                        else if ("Teacher".equalsIgnoreCase(role)) teacherCount++;
                        else if ("Parent".equalsIgnoreCase(role)) parentCount++;
                    }

                    int total = studentCount + teacherCount + parentCount;

                    HomeModel model = new HomeModel("home_data", total, studentCount, teacherCount, parentCount);
                    new Thread(() -> homeDao.insertHomeInfo(model)).start();
                })
                .addOnFailureListener(e ->
                        Log.e("HOME_FIRESTORE", "Error fetching user stats", e));
    }
}
