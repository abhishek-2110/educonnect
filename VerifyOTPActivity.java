package com.example.fbauth.AuthenticationFiles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText txtOTP;
    private Button btnVerifyOTP;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // Initialize UI components
        txtOTP = findViewById(R.id.txt_otp);
        btnVerifyOTP = findViewById(R.id.btn_verify_otp);
        mAuth = FirebaseAuth.getInstance();

        // Retrieve data from Intent
        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (verificationId == null) {
            Toast.makeText(this, "Error: Verification ID is missing.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Verify OTP Button
        btnVerifyOTP.setOnClickListener(v -> {
            String otp = txtOTP.getText().toString().trim();
            if (otp.isEmpty() || otp.length() < 6) {
                Toast.makeText(this, "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOTP(otp);
            }
        });

        // ✅ Back/Forward Navigation
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Phone Verified! Now reset your password.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyOTPActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
