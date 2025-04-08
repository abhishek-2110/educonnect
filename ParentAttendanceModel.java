package com.example.fbauth.ParentFiles;

public class ParentAttendanceModel {
    private String subject;
    private String attendanceStatus;
    private long timestamp;

    public ParentAttendanceModel() {} // Needed for Firestore

    public String getSubject() {
        return subject;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
