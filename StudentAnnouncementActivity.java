package com.example.fbauth.Student;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentAnnouncementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAnnouncementViewAdapter adapter;
    private StudentAnnouncementViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_announcement);

        recyclerView = findViewById(R.id.recycler_student_announcement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // ✅ Step 1: Get user subject from Firestore
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String studentSubject = documentSnapshot.getString("subject");

                if (studentSubject == null || studentSubject.isEmpty()) {
                    Toast.makeText(this, "Subject not found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Step 2: Setup ViewModel to observe local Room database
                StudentAnnouncementViewModelFactory factory =
                        new StudentAnnouncementViewModelFactory(getApplication(), studentSubject);
                viewModel = new ViewModelProvider(this, factory).get(StudentAnnouncementViewModel.class);

                viewModel.getAnnouncementList().observe(this, announcementList -> {
                    adapter = new StudentAnnouncementViewAdapter(announcementList);
                    recyclerView.setAdapter(adapter);
                });

                // ✅ Step 3: Fetch from Firestore and insert into Room
                db.collection("announcements")
                        .whereEqualTo("subject", studentSubject)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            List<StudentAnnouncementModel> roomList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                StudentAnnouncementModel model = new StudentAnnouncementModel();
                                model.setSubject(doc.getString("subject"));
                                model.setText(doc.getString("text"));

                                Object timestampObj = doc.get("timestamp");
                                String formattedDate = "N/A";

                                if (timestampObj instanceof Timestamp) {
                                    formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                            .format(((Timestamp) timestampObj).toDate());
                                } else if (timestampObj instanceof Long) {
                                    formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                            .format(new java.util.Date((Long) timestampObj));
                                }

                                model.setTimestamp(formattedDate);
                                roomList.add(model);
                            }

                            new Thread(() -> {
                                StudentAppDatabase.getInstance(this)
                                        .studentAnnouncementDao()
                                        .clearAll();
                                StudentAppDatabase.getInstance(this)
                                        .studentAnnouncementDao()
                                        .insertAll(roomList);
                            }).start();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to load announcements", Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(this, "Student document missing!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show());

        // 🔙🔜 Back/Forward buttons
        findViewById(R.id.btnBack).setOnClickListener(v -> NavigationController.goBack(this));
        findViewById(R.id.btnForward).setOnClickListener(v -> NavigationController.goForward(this));
    }
}
