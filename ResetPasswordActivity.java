package com.example.fbauth.AuthenticationFiles;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText txtNewPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txtNewPassword = findViewById(R.id.txt_new_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(v -> {
            String newPassword = txtNewPassword.getText().toString().trim();
            if (newPassword.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.updatePassword(newPassword)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Password Reset Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // ✅ Navigation buttons
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }
}
