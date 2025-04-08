package com.example.fbauth.Student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_announcement_table")
public class StudentAnnouncementModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String subject;
    private String text;
    private String timestamp;

    // ✅ Setters
    public void setId(int id) { this.id = id; }

    public void setSubject(String subject) { this.subject = subject; }

    public void setText(String text) { this.text = text; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    // ✅ Getters
    public int getId() { return id; }

    public String getSubject() { return subject; }

    public String getText() { return text; }

    public String getTimestamp() { return timestamp; }
}
