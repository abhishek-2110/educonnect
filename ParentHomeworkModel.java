package com.example.fbauth.ParentFiles;

public class ParentHomeworkModel {
    private String subject;
    private String homework;
    private long timestamp;

    public ParentHomeworkModel(String subject, String homework, long timestamp) {
        this.subject = subject;
        this.homework = homework;
        this.timestamp = timestamp;
    }

    public String getSubject() {
        return subject;
    }

    public String getHomework() {
        return homework;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
