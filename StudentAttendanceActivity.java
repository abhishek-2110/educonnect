package com.example.fbauth.Student;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAttendanceViewAdapter adapter;
    private StudentAppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        recyclerView = findViewById(R.id.recycler_student_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = StudentAppDatabase.getInstance(this);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {
            if (userDoc.exists()) {
                String userId = userDoc.getString("userId");

                if (userId == null || userId.isEmpty()) {
                    Toast.makeText(this, "User ID missing in Firestore!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Fetch attendance records for this user
                db.collection("attendance")
                        .whereEqualTo("studentId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<StudentAttendanceModel> attendanceList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                StudentAttendanceModel model = new StudentAttendanceModel();
                                model.setStudentId(doc.getString("studentId"));
                                model.setStudentName(doc.getString("studentName"));
                                model.setSubject(doc.getString("subject"));

                                Object timestampObj = doc.get("timestamp");
                                String dateString = "N/A";

                                if (timestampObj instanceof com.google.firebase.Timestamp) {
                                    Timestamp ts = (Timestamp) timestampObj;
                                    dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(ts.toDate());

                                } else if (timestampObj instanceof Long) {
                                    Long millis = (Long) timestampObj;
                                    dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new java.util.Date(millis));

                                } else if (timestampObj instanceof String) {
                                    dateString = (String) timestampObj; // fallback
                                }

                                model.setDate(dateString);


                                model.setStatus(doc.getString("attendanceStatus"));
                                attendanceList.add(model);
                            }

                            // ✅ Save to Room
                            new Thread(() -> {
                                database.studentAttendanceDao().clearAll();
                                database.studentAttendanceDao().insertAll(attendanceList);
                            }).start();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Attendance fetch failed", Toast.LENGTH_SHORT).show());

                // ✅ Observe Room database for live updates
                database.studentAttendanceDao().getAttendanceForStudent(userId)
                        .observe(this, new Observer<List<StudentAttendanceModel>>() {
                            @Override
                            public void onChanged(List<StudentAttendanceModel> models) {
                                adapter = new StudentAttendanceViewAdapter(models);
                                recyclerView.setAdapter(adapter);
                            }
                        });

            } else {
                Toast.makeText(this, "User document missing!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error accessing user data", Toast.LENGTH_SHORT).show());

        // Navigation
        findViewById(R.id.btnBack).setOnClickListener(v -> NavigationController.goBack(this));
        findViewById(R.id.btnForward).setOnClickListener(v -> NavigationController.goForward(this));
    }
}
