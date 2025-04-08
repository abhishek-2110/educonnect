package com.example.fbauth.TeacherFiles;

public class AttendanceModel {
    private String studentId;
    private String studentName;
    private String subject;
    private String date;
    private String time;
    private String status;

    public AttendanceModel() {
        // Needed for Firestore
    }

    public AttendanceModel(String studentId, String studentName, String subject, String date, String time, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
