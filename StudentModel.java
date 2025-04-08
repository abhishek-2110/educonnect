package com.example.fbauth.Student;

public class StudentModel {
    private String studentId;
    private String studentName;
    private String email;
    private String phone;
    private String teacherId;
    private String teacherName;
    private String subject;
    private String date;
    private String time;
    private String attendanceStatus;
    private String homework; // ✅ NEW FIELD

    // Required Default Constructor for Firebase
    public StudentModel() {}

    // Full Constructor
    public StudentModel(String studentId, String studentName, String email, String phone,
                        String teacherId, String teacherName, String subject,
                        String date, String time, String attendanceStatus) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.attendanceStatus = attendanceStatus;
        this.homework = ""; // default empty
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public String getAttendanceStatus() { return attendanceStatus; }
    public String getHomework() { return homework; }

    // Setters
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }
}
