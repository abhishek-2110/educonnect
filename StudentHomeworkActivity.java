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

public class StudentHomeworkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentHomeworkViewAdapter adapter;
    private StudentHomeworkViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework);

        recyclerView = findViewById(R.id.recycler_student_homework);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String studentId = documentSnapshot.getString("userId");

                if (studentId == null || studentId.isEmpty()) {
                    Toast.makeText(this, "Student ID not found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Room ViewModel
                StudentHomeworkViewModelFactory factory =
                        new StudentHomeworkViewModelFactory(getApplication(), studentId);
                viewModel = new ViewModelProvider(this, factory).get(StudentHomeworkViewModel.class);

                viewModel.getHomeworkList().observe(this, homeworkList -> {
                    adapter = new StudentHomeworkViewAdapter(homeworkList);
                    recyclerView.setAdapter(adapter);
                });

                // ✅ Fetch from Firestore & update Room
                db.collection("homework")
                        .whereEqualTo("studentId", studentId)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            List<StudentHomeworkModel> roomList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                StudentHomeworkModel model = new StudentHomeworkModel();
                                model.setStudentId(doc.getString("studentId"));
                                model.setStudentName(doc.getString("studentName"));
                                model.setSubject(doc.getString("subject"));
                                model.setHomework(doc.getString("homework"));

                                Object tsObj = doc.get("timestamp");
                                String formattedTime = "N/A";
                                if (tsObj instanceof Timestamp) {
                                    formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                            .format(((Timestamp) tsObj).toDate());
                                } else if (tsObj instanceof Long) {
                                    formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                            .format(new java.util.Date((Long) tsObj));
                                }

                                model.setTimestamp(formattedTime);
                                roomList.add(model);
                            }

                            // ✅ Cache in Room
                            new Thread(() -> {
                                StudentAppDatabase.getInstance(this)
                                        .studentHomeworkDao()
                                        .clearAll();
                                StudentAppDatabase.getInstance(this)
                                        .studentHomeworkDao()
                                        .insertAll(roomList);
                            }).start();

                        }).addOnFailureListener(e ->
                                Toast.makeText(this, "Homework fetch failed", Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(this, "Student document missing!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show());

        // Navigation
        findViewById(R.id.btnBack).setOnClickListener(v -> NavigationController.goBack(this));
        findViewById(R.id.btnForward).setOnClickListener(v -> NavigationController.goForward(this));
    }
}
