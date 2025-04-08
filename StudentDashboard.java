package com.example.fbauth.Student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.AuthenticationFiles.Loginpage;
import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;

public class StudentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // ✅ Toolbar Buttons
        View toolbar = findViewById(R.id.main_toolbar);
        ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
        ImageButton btnLogout = toolbar.findViewById(R.id.btnLogout);
        ImageView imgHome = toolbar.findViewById(R.id.imgHome);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(StudentDashboard.this, Loginpage.class));
            finish();
        });

        imgHome.setOnClickListener(v ->
                Toast.makeText(this, "Home screen coming soon", Toast.LENGTH_SHORT).show());

        // ✅ Dashboard Feature Buttons
        Button btnAssignments = findViewById(R.id.btn_view_assignments);
        Button btnAnnouncements = findViewById(R.id.btn_view_announcements);
        Button btnContactTeacher = findViewById(R.id.btn_contact_teacher);
        Button btnAttendance = findViewById(R.id.btn_view_attendance);
        Button btnHomework = findViewById(R.id.btn_view_homework);

        btnAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, ViewAssignmentsActivity.class)));

        btnAnnouncements.setOnClickListener(v ->
                startActivity(new Intent(this, StudentAnnouncementActivity.class)));

        btnContactTeacher.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:")); // Only open email apps

            try {
                startActivity(Intent.createChooser(emailIntent, "Open email app..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email apps found.", Toast.LENGTH_SHORT).show();
            }
        });



        btnAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, StudentAttendanceActivity.class)));

        btnHomework.setOnClickListener(v ->
                startActivity(new Intent(this, StudentHomeworkActivity.class)));
    }
}
