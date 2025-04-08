package com.example.fbauth.TeacherFiles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeacherMessageViewModel extends AndroidViewModel {

    private final TeacherMessageDao messageDao;
    private final LiveData<List<TeacherMessageModel>> messagesLiveData;
    private final FirebaseFirestore firestore;
    private final ExecutorService executor;

    public TeacherMessageViewModel(@NonNull Application application, String teacherId) {
        super(application);
        TeacherAppDatabase db = TeacherAppDatabase.getInstance(application);
        this.messageDao = db.teacherMessageDao();
        this.firestore = FirebaseFirestore.getInstance();
        this.executor = Executors.newSingleThreadExecutor();
        this.messagesLiveData = messageDao.getAllMessages();

        fetchMessagesFromFirestore(teacherId);
    }

    public LiveData<List<TeacherMessageModel>> getMessagesForTeacher() {
        return messagesLiveData;
    }

    private void fetchMessagesFromFirestore(String teacherId) {
        firestore.collection("messages")
                .whereEqualTo("teacherId", teacherId)
                .get()
                .addOnSuccessListener(query -> {
                    List<TeacherMessageModel> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : query) {
                        messages.add(new TeacherMessageModel(
                                doc.getId(),
                                doc.getString("fromId"),
                                doc.getString("fromRole"),
                                doc.getString("message"),
                                doc.getString("reply"),
                                doc.getString("teacherId")
                        ));
                    }

                    executor.execute(() -> {
                        try {
                            messageDao.clearAll();
                            messageDao.insertMessages(messages);
                        } catch (Exception e) {
                            Log.e("TEACHER_MSG", "Room insert failed", e);
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e("TEACHER_MSG", "Firestore fetch failed", e));
    }


}
