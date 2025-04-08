package com.example.fbauth.AuthenticationFiles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText txtInput;
    private Button btnNext;
    private RadioButton rbEmail, rbPhone;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtInput = findViewById(R.id.txt_input);
        btnNext = findViewById(R.id.btn_next);
        rbEmail = findViewById(R.id.rb_email);
        rbPhone = findViewById(R.id.rb_phone);
        mAuth = FirebaseAuth.getInstance();

        btnNext.setOnClickListener(v -> {
            String input = txtInput.getText().toString().trim();

            if (input.isEmpty()) {
                Toast.makeText(this, "Enter your Email or Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rbEmail.isChecked()) {
                resetPasswordByEmail(input);
            } else if (rbPhone.isChecked()) {
                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOTPActivity.class);
                intent.putExtra("phoneNumber", input);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a recovery method", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Back/Forward buttons
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void resetPasswordByEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Reset link sent to email", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
