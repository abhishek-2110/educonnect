package com.example.fbauth.TeacherFiles;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "teacher_messages")
public class TeacherMessageModel {

    @PrimaryKey
    @NonNull
    public String id;

    public String fromId;
    public String fromRole;
    public String message;
    public String reply;
    public String teacherId;

    public TeacherMessageModel(String id, String fromId, String fromRole, String message, String reply, String teacherId) {
        this.id = id;
        this.fromId = fromId;
        this.fromRole = fromRole;
        this.message = message;
        this.reply = reply;
        this.teacherId = teacherId;
    }
}
