package com.example.fbauth.Student;

public class SubmissionModel {
    private String assignmentId;
    private String studentId;
    private String answer;
    private String mark;
    private String feedback;

    public SubmissionModel() {}

    public SubmissionModel(String assignmentId, String studentId, String answer, String mark, String feedback) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.answer = answer;
        this.mark = mark;
        this.feedback = feedback;
    }

    public String getAssignmentId() { return assignmentId; }
    public String getStudentId() { return studentId; }
    public String getAnswer() { return answer; }
    public String getMark() { return mark; }
    public String getFeedback() { return feedback; }
}
