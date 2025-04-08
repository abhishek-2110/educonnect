package com.example.fbauth.Student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_homework_table")
public class StudentHomeworkModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String studentId;
    private String studentName;
    private String email;
    private String subject;
    private String homework;
    private String timestamp;

    // ✅ Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // ✅ Getters
    public int getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getHomework() {
        return homework;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
