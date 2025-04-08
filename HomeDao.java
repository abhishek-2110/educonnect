package com.example.fbauth.HomeFiles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHomeInfo(HomeModel model);

    @Query("SELECT * FROM home_info_table WHERE id = 'home_data'")
    LiveData<HomeModel> getHomeInfo();
}
