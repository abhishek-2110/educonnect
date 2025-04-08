package com.example.fbauth.TeacherFiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fbauth.NavigationController;
import com.example.fbauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UploadAssignmentActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 101;
    private EditText edtTitle, edtDescription;
    private Button btnChooseFile, btnUploadAssignment, btnBack, btnForward;
    private Uri fileUri;
    private ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnUploadAssignment = findViewById(R.id.btn_upload_assignment);
        btnBack = findViewById(R.id.btnBack);
        btnForward = findViewById(R.id.btnForward);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("assignments");
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnChooseFile.setOnClickListener(v -> openFilePicker());

        btnUploadAssignment.setOnClickListener(v -> {
            if (fileUri == null) {
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            } else {
                uploadAssignmentFile();
            }
        });

        // 🔙🔜 Back and Forward navigation support
        btnBack.setOnClickListener(v -> NavigationController.goBack(this));
        btnForward.setOnClickListener(v -> NavigationController.goForward(this));
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            Toast.makeText(this, "File selected: " + fileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAssignmentFile() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Title and description are required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        String fileName = System.currentTimeMillis() + "." +
                MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(fileUri));

        StorageReference fileRef = storageRef.child(fileName);
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String assignmentId = db.collection("assignments").document().getId();

                    Map<String, Object> assignment = new HashMap<>();
                    assignment.put("assignmentId", assignmentId);
                    assignment.put("title", title);
                    assignment.put("description", description);
                    assignment.put("fileUrl", uri.toString());
                    assignment.put("fileName", fileName);
                    assignment.put("teacherId", auth.getCurrentUser().getUid());
                    assignment.put("timestamp", System.currentTimeMillis());

                    db.collection("assignments").document(assignmentId)
                            .set(assignment)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Assignment uploaded", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
