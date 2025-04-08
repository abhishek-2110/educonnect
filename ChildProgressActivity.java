package com.example.fbauth.ParentFiles;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.example.fbauth.Student.SubmissionAdapter;
import com.example.fbauth.Student.SubmissionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ChildProgressActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubmissionAdapter adapter;
    private List<SubmissionModel> submissionList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_progress);

        recyclerView = findViewById(R.id.recycler_child_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        submissionList = new ArrayList<>();

        fetchChildSubmissions();

        // ✅ Navigation Buttons
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void fetchChildSubmissions() {
        String parentEmail = auth.getCurrentUser().getEmail();

        db.collection("users")
                .whereEqualTo("role", "Student")
                .whereEqualTo("parentEmail", parentEmail)
                .get()
                .addOnSuccessListener(studentSnapshots -> {
                    if (studentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No child linked to this parent!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot studentDoc : studentSnapshots) {
                        String studentId = studentDoc.getString("userId");

                        db.collection("submissions")
                                .whereEqualTo("studentId", studentId)
                                .get()
                                .addOnSuccessListener(submissionDocs -> {
                                    submissionList.clear();
                                    for (QueryDocumentSnapshot subDoc : submissionDocs) {
                                        SubmissionModel model = subDoc.toObject(SubmissionModel.class);
                                        submissionList.add(model);
                                    }
                                    adapter = new SubmissionAdapter(submissionList);
                                    recyclerView.setAdapter(adapter);
                                });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show());
    }
}
