package com.example.fbauth.AuthenticationFiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail, txtPass, txtName, txtPhone, etStudentId;
    private Spinner spinnerRole, spinnerSubject;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedRole = "Student";
    private String selectedSubject = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_pass);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        etStudentId = findViewById(R.id.et_student_id); // new field
        spinnerRole = findViewById(R.id.spinner_role);
        spinnerSubject = findViewById(R.id.spinner_subject);
        btnRegister = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Hide studentId field by default
        etStudentId.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this,
                R.array.subjects, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();

                // Show or hide Student ID field based on role
                if ("Parent".equals(selectedRole)) {
                    etStudentId.setVisibility(View.VISIBLE);
                } else {
                    etStudentId.setVisibility(View.GONE);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnRegister.setOnClickListener(v -> registerUser());

        findViewById(R.id.btnBack).setOnClickListener(v -> NavigationController.goBack(this));
        //findViewById(R.id.btnForward).setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void registerUser() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim(); // Get student ID

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || selectedSubject.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Parent".equals(selectedRole) && studentId.isEmpty()) {
            Toast.makeText(this, "Please enter student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = mAuth.getCurrentUser().getUid();
                String roleIdPrefix = selectedRole.equals("Teacher") ? "TEACH" :
                        selectedRole.equals("Parent") ? "PARENT" : "STU";

                DocumentReference countRef = db.collection("metadata").document("userCount");
                countRef.get().addOnSuccessListener(document -> {
                    long lastId = document.exists() ? document.getLong("count") : 0;
                    String generatedId = roleIdPrefix + String.format("%03d", lastId + 1);

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("role", selectedRole);
                    user.put("subject", selectedSubject);
                    user.put("userId", generatedId);
                    user.put("teacherId", selectedRole.equals("Student") ? "DEFAULT_TEACHER_ID" : userId);

                    if ("Parent".equals(selectedRole)) {
                        user.put("studentId", studentId); // Link to child
                    }

                    db.collection("users").document(userId).set(user)
                            .addOnSuccessListener(aVoid -> {
                                countRef.update("count", lastId + 1)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, Loginpage.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("RegisterActivity", "Failed to update userCount", e);
                                            Toast.makeText(this, "Error updating user count", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("RegisterActivity", "Error saving user", e);
                                Toast.makeText(this, "Error saving user", Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Log.e("RegisterActivity", "Error fetching userCount", e);
                    Toast.makeText(this, "Failed to fetch user count", Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.e("RegisterActivity", "Firebase Auth failed", task.getException());
                Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
