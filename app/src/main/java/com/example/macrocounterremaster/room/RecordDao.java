package com.example.macrocounterremaster.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.macrocounterremaster.models.NoteModel;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM notes")
    List<NoteModel> getAll();

    @Query("SELECT * FROM notes WHERE month LIKE :month " +
            "AND day LIKE :day AND year like :year")
    List<NoteModel> findByDate(String month, String day, String year);

    @Query("SELECT COUNT(*) from notes")
    int countUsers();

    @Insert
    void insertAll(NoteModel... noteModels);

    @Delete
    void delete(NoteModel noteModel);

    @Query("DELETE FROM notes")
    public void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecord(NoteModel noteModel);
}
