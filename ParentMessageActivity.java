package com.example.fbauth.ParentFiles;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ParentMessageActivity extends AppCompatActivity {

    private EditText etMessage;
    private Button btnSendMessage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String parentId;
    private String studentId;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_message);

        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // 🔹 Step 1: Get current parent info
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnSuccessListener(parentDoc -> {
            if (parentDoc.exists()) {
                parentId = parentDoc.getString("userId");
                studentId = parentDoc.getString("studentId");

                if (TextUtils.isEmpty(studentId)) {
                    Toast.makeText(this, "Linked student ID not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                // 🔹 Step 2: Get teacherId from student document
                db.collection("users")
                        .whereEqualTo("userId", studentId)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            for (DocumentSnapshot studentDoc : querySnapshot) {
                                teacherId = studentDoc.getString("teacherId");

                                if (TextUtils.isEmpty(teacherId)) {
                                    Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }

                                // 🔹 Enable message sending
                                btnSendMessage.setOnClickListener(v -> sendMessage());
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to fetch student info", Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(this, "Parent profile not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> message = new HashMap<>();
        message.put("message", messageText);
        message.put("fromId", parentId);
        message.put("teacherId", "waKsMRkXg9OnOB5wkmyR2EiUyUU2"); // ✅ Changed from "toId" to "teacherId"
        message.put("fromRole", "Parent");
        message.put("timestamp", System.currentTimeMillis());
        message.put("reply", "");

        db.collection("messages")
                .add(message)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Message sent to teacher", Toast.LENGTH_SHORT).show();
                    etMessage.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show());
    }
}
