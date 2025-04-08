package com.example.fbauth.TeacherFiles;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeacherMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private EditText etReply;
    private Button btnSendReply;
    private MessageAdapter adapter;

    private FirebaseFirestore firestore;
    private final String hardcodedTeacherId = "waKsMRkXg9OnOB5wkmyR2EiUyUU2"; // 🔐 Hardcoded teacherId

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_message);

        initViews();
        firestore = FirebaseFirestore.getInstance();

        fetchMessagesFromFirestore();
    }

    private void initViews() {
        recyclerMessages = findViewById(R.id.recycler_messages);
        etReply = findViewById(R.id.et_reply);
        btnSendReply = findViewById(R.id.btn_send_reply);

        adapter = new MessageAdapter();
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(adapter);
    }

    private void fetchMessagesFromFirestore() {
        firestore.collection("messages")
                .whereEqualTo("teacherId", hardcodedTeacherId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<TeacherMessageModel> messageList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        TeacherMessageModel message = new TeacherMessageModel(
                                doc.getId(),
                                doc.getString("fromId"),
                                doc.getString("fromRole"),
                                doc.getString("message"),
                                doc.getString("reply"),
                                doc.getString("teacherId")
                        );
                        messageList.add(message);
                    }

                    adapter.setMessages(messageList);
                    Log.d("TEACHER_MSG", "Loaded messages: " + messageList.size());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                    Log.e("TEACHER_MSG", "Firestore fetch failed", e);
                });
    }
}
