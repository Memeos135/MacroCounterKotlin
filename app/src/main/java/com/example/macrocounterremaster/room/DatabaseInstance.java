package com.example.macrocounterremaster.room;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.macrocounterremaster.models.NoteModel;

@Database(entities = {NoteModel.class}, version = 1)
public abstract class DatabaseInstance extends RoomDatabase {

    private static final String LOG_TAG = DatabaseInstance.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "NotesDatabase";
    private static DatabaseInstance sInstance;

    public static DatabaseInstance getInstance(Context context){

        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DatabaseInstance.class, DatabaseInstance.DATABASE_NAME)
                        // DELETE WHEN FINISHED TESTING
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecordDao recordDao();
}
