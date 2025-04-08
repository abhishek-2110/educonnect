package com.example.fbauth.TeacherFiles;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;
import com.example.fbauth.Student.StudentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<StudentModel> studentList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Button btnSaveAttendance;
    private String teacherSubject = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView = findViewById(R.id.recycler_attendance);
        btnSaveAttendance = findViewById(R.id.btn_save_attendance);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchTeacherSubject();

        btnSaveAttendance.setOnClickListener(v -> saveAttendance());
    }

    private void fetchTeacherSubject() {
        String teacherId = auth.getCurrentUser().getUid();
        db.collection("users").document(teacherId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        teacherSubject = documentSnapshot.getString("subject");
                        fetchStudentsBySubject();
                    } else {
                        Toast.makeText(this, "Teacher not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to get teacher info", Toast.LENGTH_SHORT).show());
    }

    private void fetchStudentsBySubject() {
        db.collection("users")
                .whereEqualTo("role", "Student")
                .whereEqualTo("subject", teacherSubject)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    studentList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        StudentModel student = new StudentModel(
                                doc.getString("userId"),
                                doc.getString("name"),
                                doc.getString("email"),
                                doc.getString("phone"),
                                doc.getString("teacherId"),
                                "",
                                teacherSubject,
                                "", "", ""
                        );
                        studentList.add(student);
                    }
                    adapter = new AttendanceAdapter(studentList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show());
    }

    private void saveAttendance() {
        CollectionReference attendanceRef = db.collection("attendance");
        for (StudentModel student : studentList) {
            if (student.getAttendanceStatus() != null && !student.getAttendanceStatus().isEmpty()) {
                Map<String, Object> record = new HashMap<>();
                record.put("studentId", student.getStudentId());
                record.put("studentName", student.getStudentName());
                record.put("email", student.getEmail());
                record.put("attendanceStatus", student.getAttendanceStatus());
                record.put("subject", teacherSubject);
                record.put("timestamp", System.currentTimeMillis());

                attendanceRef.add(record);
            }
        }
        Toast.makeText(this, "Attendance saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
