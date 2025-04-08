package com.example.fbauth.TeacherFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.example.fbauth.Student.StudentModel;
import com.example.fbauth.AuthenticationFiles.Loginpage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeacherDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnMarkAttendance, btnAssignHomework, btnUploadAssignment, btnSendAnnouncement;
    private FirebaseFirestore db;
    private List<StudentModel> studentList;
    private AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // 🔹 View Bindings
        recyclerView = findViewById(R.id.recycler_students_dashboard);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        btnAssignHomework = findViewById(R.id.btnAssignHomework);
        btnUploadAssignment = findViewById(R.id.btnUploadAssignment);
        btnSendAnnouncement = findViewById(R.id.btnSendAnnouncement);

        // 🔹 Toolbar buttons (ImageButtons & ImageView)
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        ImageView imgHome = findViewById(R.id.imgHome);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TeacherDashboard.this, Loginpage.class));
            finish();
        });

        imgHome.setOnClickListener(v -> {
            Toast.makeText(this, "Home screen coming soon", Toast.LENGTH_SHORT).show();
        });

        // 🔹 Firestore Setup
        db = FirebaseFirestore.getInstance();
        studentList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchAllStudents();

        // 🔹 Button Click Handlers
        btnMarkAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceActivity.class)));

        btnAssignHomework.setOnClickListener(v ->
                startActivity(new Intent(this, AssignHomeworkActivity.class)));

        btnUploadAssignment.setOnClickListener(v ->
                startActivity(new Intent(this, UploadAssignmentActivity.class)));

        btnSendAnnouncement.setOnClickListener(v ->
                startActivity(new Intent(this, AnnouncementsActivity.class)));

        // ✅ NEW: Message Button Click Handler
        findViewById(R.id.btn_open_messages).setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherMessageActivity.class));
        });
    }

    private void fetchAllStudents() {
        String teacherUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(teacherUid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String teacherSubject = documentSnapshot.getString("subject");

                if (teacherSubject == null || teacherSubject.isEmpty()) return;

                db.collection("users")
                        .whereEqualTo("role", "Student")
                        .whereEqualTo("subject", teacherSubject)
                        .get()
                        .addOnSuccessListener(task -> {
                            studentList.clear();
                            for (QueryDocumentSnapshot document : task) {
                                studentList.add(new StudentModel(
                                        document.getString("userId"),
                                        document.getString("name"),
                                        document.getString("email"),
                                        document.getString("phone"),
                                        "", "",
                                        document.getString("subject"),
                                        "", "", ""
                                ));
                            }
                            adapter = new AttendanceAdapter(studentList);
                            recyclerView.setAdapter(adapter);
                        });
            }
        });
    }
}
