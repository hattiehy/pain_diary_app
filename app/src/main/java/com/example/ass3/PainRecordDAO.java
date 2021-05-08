package com.example.ass3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ass3.entity.PainRecord;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PainRecordDAO {
    @Query("SELECT * FROM PainRecord ORDER BY uid ASC")
    LiveData<List<PainRecord>> getAll();

    @Query("SELECT * FROM PainRecord ORDER BY uid ASC")
    List<PainRecord> getAllR();

    @Query("SELECT pain_location FROM PainRecord")
    List<String> getPainLoc();

    @Query("SELECT * FROM PainRecord WHERE uid = :recordId LIMIT 1")
    PainRecord findByID(int recordId);

    @Query("SELECT * FROM PainRecord WHERE recordDate = :date LIMIT 1")
    PainRecord findByDate(String date);

    @Insert
    void insert(PainRecord painRecord);

    @Delete
    void delete(PainRecord painRecord);

    @Update
    void updateRecord(PainRecord painRecord);

    @Query("DELETE FROM PainRecord")
    void deleteAll();
}
