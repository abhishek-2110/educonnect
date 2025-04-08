package com.example.fbauth.ParentFiles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;

import com.example.fbauth.AuthenticationFiles.Loginpage;

import com.google.firebase.auth.FirebaseAuth;

public class ParentDashboard extends AppCompatActivity {

    private Button btnViewChildProgress, btnMessageTeacher, btnViewAttendance, btnViewHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        // ✅ Bind custom toolbar buttons
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        ImageView imgHome = findViewById(R.id.imgHome);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Loginpage.class));
            finish();
        });

        imgHome.setOnClickListener(v ->
                Toast.makeText(this, "Home screen coming soon", Toast.LENGTH_SHORT).show()
        );

        // 🔹 Initialize Main Dashboard Buttons
        btnViewChildProgress = findViewById(R.id.btn_child_progress);

        btnMessageTeacher = findViewById(R.id.btn_message_teacher);
        btnViewAttendance = findViewById(R.id.btn_view_attendance);
        btnViewHomework = findViewById(R.id.btn_view_homework);

        btnViewChildProgress.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboard.this, ChildProgressActivity.class);
            startActivity(intent);
        });



        btnMessageTeacher.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboard.this, ParentMessageActivity.class);
            startActivity(intent);
        });

        btnViewAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboard.this, ParentAttendanceActivity.class);
            startActivity(intent);
        });

        btnViewHomework.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboard.this, ParentHomeworkActivity.class);
            startActivity(intent);
        });
    }
}
