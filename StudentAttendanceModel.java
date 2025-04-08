package com.example.fbauth.Student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance_table")
public class StudentAttendanceModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String studentId;
    private String studentName;
    private String subject;
    private String date;
    private String status;

    // Setters
    public void setId(int id) { this.id = id; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public void setSubject(String subject) { this.subject = subject; }

    public void setDate(String date) { this.date = date; }

    public void setStatus(String status) { this.status = status; }

    // Getters
    public int getId() { return id; }

    public String getStudentId() { return studentId; }

    public String getStudentName() { return studentName; }

    public String getSubject() { return subject; }

    public String getDate() { return date; }

    public String getStatus() { return status; }
}
