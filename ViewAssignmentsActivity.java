package com.example.fbauth.Student;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.example.fbauth.TeacherFiles.AssignmentAdapter;
import com.example.fbauth.TeacherFiles.AssignmentModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAssignmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private FirebaseFirestore db;
    private List<AssignmentModel> assignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignments);

        recyclerView = findViewById(R.id.recycler_assignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        assignmentList = new ArrayList<>();

        fetchAssignments();

        // ✅ Back/Forward Navigation
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void fetchAssignments() {
        db.collection("assignments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        assignmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AssignmentModel assignment = document.toObject(AssignmentModel.class);
                            assignmentList.add(assignment);
                        }
                        adapter = new AssignmentAdapter(assignmentList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Error loading assignments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
