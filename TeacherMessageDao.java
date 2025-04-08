package com.example.fbauth.TeacherFiles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TeacherMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<TeacherMessageModel> messages);

    @Query("DELETE FROM teacher_messages")
    void clearAll();

    @Query("SELECT * FROM teacher_messages")
    LiveData<List<TeacherMessageModel>> getAllMessages();
}
