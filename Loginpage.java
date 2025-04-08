package com.example.fbauth.AuthenticationFiles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.ParentFiles.ParentDashboard;
import com.example.fbauth.R;
import com.example.fbauth.Student.StudentDashboard;
import com.example.fbauth.TeacherFiles.TeacherDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Loginpage extends AppCompatActivity {

    private EditText txtEmail, txtPass;
    private Button btnLogin;
    private TextView tvRegister, tv_forgot;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_pass);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tv_forgot = findViewById(R.id.tv_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Loginpage.this, RegisterActivity.class);
            startActivity(intent);
        });

        tv_forgot.setOnClickListener(v -> {
            Intent intent = new Intent(Loginpage.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Loginpage.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(Loginpage.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // ✅ Navigation Buttons
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void checkUserRole(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");

                        if ("Teacher".equals(role)) {
                            startActivity(new Intent(Loginpage.this, TeacherDashboard.class));
                        } else if ("Student".equals(role)) {
                            startActivity(new Intent(Loginpage.this, StudentDashboard.class));
                        } else if ("Parent".equals(role)) {
                            startActivity(new Intent(Loginpage.this, ParentDashboard.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(Loginpage.this, "User data not found!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Loginpage.this, "Failed to fetch user role!", Toast.LENGTH_LONG).show());
    }
}
