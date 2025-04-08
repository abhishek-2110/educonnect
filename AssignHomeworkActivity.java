package com.example.fbauth.TeacherFiles;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

public class AssignHomeworkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private List<StudentModel> studentList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String teacherSubject = "";
    private Button btnSaveHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_homework);

        recyclerView = findViewById(R.id.recycler_assign_homework);
        btnSaveHomework = findViewById(R.id.btn_save_homework);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchTeacherSubject();

        btnSaveHomework.setOnClickListener(v -> saveHomeworkToFirestore());
    }

    private void fetchTeacherSubject() {
        String teacherId = auth.getCurrentUser().getUid();
        db.collection("users").document(teacherId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        teacherSubject = document.getString("subject");
                        fetchStudentsBySubject();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to get subject", Toast.LENGTH_SHORT).show());
    }

    private void fetchStudentsBySubject() {
        db.collection("users")
                .whereEqualTo("role", "Student")
                .whereEqualTo("subject", teacherSubject)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    studentList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        studentList.add(new StudentModel(
                                doc.getString("userId"),
                                doc.getString("name"),
                                doc.getString("email"),
                                doc.getString("phone"),
                                "", "", teacherSubject,
                                "", "", "" // No attendance/date/time needed here
                        ));
                    }
                    adapter = new HomeworkAdapter(studentList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show());
    }

    private void saveHomeworkToFirestore() {
        CollectionReference homeworkRef = db.collection("homework");

        for (StudentModel student : studentList) {
            if (student.getHomework() == null || student.getHomework().trim().isEmpty()) continue;

            Map<String, Object> hw = new HashMap<>();
            hw.put("studentId", student.getStudentId());
            hw.put("studentName", student.getStudentName());
            hw.put("email", student.getEmail());
            hw.put("homework", student.getHomework());
            hw.put("subject", teacherSubject);
            hw.put("timestamp", System.currentTimeMillis());

            homeworkRef.add(hw);
        }

        Toast.makeText(this, "Homework assigned to students!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
