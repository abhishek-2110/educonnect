package com.example.fbauth;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.tv_welcome);

        // Get the email passed from LoginActivity
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("email");
        if (userEmail != null) {
            welcomeText.setText("Welcome, " + userEmail + "!");
        }
    }
}


