package com.example.fbauth.HomeFiles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fbauth.AuthenticationFiles.Loginpage;
import com.example.fbauth.R;

public class HomeActivity extends AppCompatActivity {

    private TextView txtTotalUsers, txtStudents, txtTeachers, txtParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtTotalUsers = findViewById(R.id.txtTotalUsers);
        txtStudents = findViewById(R.id.txtStudents);
        txtTeachers = findViewById(R.id.txtTeachers);
        txtParents = findViewById(R.id.txtParents);

        Button btnVisitWebsite = findViewById(R.id.btnVisitWebsite);
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnVisitWebsite.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.op.ac.nz/"))));

        btnGoToLogin.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, Loginpage.class)));

        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getHomeInfo().observe(this, model -> {
            if (model != null) {
                txtTotalUsers.setText("Total Registered Users: " + model.totalUsers);
                txtStudents.setText("Students: " + model.totalStudents);
                txtTeachers.setText("Teachers: " + model.totalTeachers);
                txtParents.setText("Parents: " + model.totalParents);
            }
        });
    }
}
