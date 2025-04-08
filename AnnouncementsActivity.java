package com.example.fbauth.TeacherFiles;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnouncementsActivity extends AppCompatActivity {

    private EditText edtAnnouncement;
    private Button btnSendAnnouncement;
    private RecyclerView recyclerAnnouncements;
    private AnnouncementAdapter adapter;
    private List<String> announcementList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String currentUserId;
    private String currentUserRole = "";
    private String currentUserSubject = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        edtAnnouncement = findViewById(R.id.et_announcement);
        btnSendAnnouncement = findViewById(R.id.btn_post_announcement);
        recyclerAnnouncements = findViewById(R.id.recycler_announcements);

        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter(announcementList);
        recyclerAnnouncements.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnnouncements.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Navigation
        findViewById(R.id.btnBack).setOnClickListener(v -> NavigationController.goBack(this));
        findViewById(R.id.btnForward).setOnClickListener(v -> NavigationController.goForward(this));

        // Check role & subject
        currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(currentUserId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        currentUserRole = document.getString("role");
                        currentUserSubject = document.getString("subject");

                        if ("Teacher".equals(currentUserRole)) {
                            edtAnnouncement.setVisibility(View.VISIBLE);
                            btnSendAnnouncement.setVisibility(View.VISIBLE);

                            btnSendAnnouncement.setOnClickListener(v -> sendAnnouncement());
                        }

                        loadAnnouncements();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load user info", Toast.LENGTH_SHORT).show()
                );
    }

    private void sendAnnouncement() {
        String text = edtAnnouncement.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "Please enter announcement text", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("subject", currentUserSubject);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("announcements")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Announcement sent", Toast.LENGTH_SHORT).show();
                    edtAnnouncement.setText("");
                    loadAnnouncements();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to send announcement", Toast.LENGTH_SHORT).show()
                );
    }

    private void loadAnnouncements() {
        db.collection("announcements")
                .whereEqualTo("subject", currentUserSubject)
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    announcementList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String msg = doc.getString("text");
                        if (msg != null) {
                            announcementList.add(msg);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load announcements", Toast.LENGTH_SHORT).show()
                );
    }
}
