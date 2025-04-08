package com.example.fbauth.ParentFiles;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParentHomeworkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParentHomeworkAdapter adapter;
    private List<ParentHomeworkModel> homeworkList;
    private FirebaseFirestore db;
    private CollectionReference homeworkRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_homework);

        recyclerView = findViewById(R.id.recycler_parent_homework);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeworkList = new ArrayList<>();
        adapter = new ParentHomeworkAdapter(homeworkList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        homeworkRef = db.collection("homework");

        fetchStudentIdAndLoadHomework();
    }

    private void fetchStudentIdAndLoadHomework() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String studentId = documentSnapshot.getString("studentId");
                        if (studentId != null && !studentId.isEmpty()) {
                            fetchHomework(studentId);
                        } else {
                            Toast.makeText(this, "Student ID not linked to parent!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Parent profile not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading parent info", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchHomework(String studentId) {
        homeworkRef.whereEqualTo("studentId", studentId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    homeworkList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String subject = doc.getString("subject");
                        String status = doc.getString("homework");
                        Long timestamp = doc.getLong("timestamp");

                        String date = convertTimestampToDate(timestamp);
                        homeworkList.add(new ParentHomeworkModel(subject, status, timestamp));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to fetch homework", Toast.LENGTH_SHORT).show()
                );
    }

    private String convertTimestampToDate(Long timestamp) {
        if (timestamp == null) return "Unknown Date";
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
