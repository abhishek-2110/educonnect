package com.example.fbauth.HomeFiles;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "home_info_table")
public class HomeModel {

    @PrimaryKey
    @NonNull
    public String id;

    public int totalUsers;
    public int totalStudents;
    public int totalTeachers;
    public int totalParents;

    public HomeModel(@NonNull String id, int totalUsers, int totalStudents, int totalTeachers, int totalParents) {
        this.id = id;
        this.totalUsers = totalUsers;
        this.totalStudents = totalStudents;
        this.totalTeachers = totalTeachers;
        this.totalParents = totalParents;
    }
}
