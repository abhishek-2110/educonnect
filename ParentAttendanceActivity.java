package com.example.fbauth.ParentFiles;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ParentAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParentAttendanceAdapter adapter;
    private List<ParentAttendanceModel> attendanceList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_attendance);

        recyclerView = findViewById(R.id.recycler_parent_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParentAttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchLinkedStudentIdAndLoadAttendance();
    }

    private void fetchLinkedStudentIdAndLoadAttendance() {
        String parentUid = auth.getCurrentUser().getUid();

        db.collection("users").document(parentUid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String studentId = documentSnapshot.getString("studentId");
                        if (studentId != null && !studentId.isEmpty()) {
                            loadAttendanceForStudent(studentId);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("ParentAttendance", "Failed to fetch parent doc", e));
    }

    private void loadAttendanceForStudent(String studentId) {
        db.collection("attendance")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        attendanceList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ParentAttendanceModel model = doc.toObject(ParentAttendanceModel.class);
                            attendanceList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("ParentAttendance", "Failed to load attendance", task.getException());
                    }
                });
    }
}
