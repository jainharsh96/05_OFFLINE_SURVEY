package io.mountblue.offlineSurvey.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mountblue.offlineSurvey.model.FormResponse;

@Dao
public interface ResponceDao {

    @Query("SELECT * FROM responcelist ORDER BY lastUpdate DESC")
    LiveData<List<FormResponse>> loadAllResponses();

    @Insert
    void insertResponce(FormResponse responce);

    @Delete
    void deleteResponce(FormResponse responce);

    @Update
    void updateResponceById(FormResponse response);

    @Query("SELECT * FROM responcelist WHERE responceId = :getId")
    LiveData<FormResponse> loadResponseById(int getId);
}
