package io.mountblue.offlineSurvey.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import io.mountblue.offlineSurvey.model.FormResponse;

@Database(entities = {FormResponse.class}, version = 1, exportSchema = false)

public abstract class ResponceDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ResponseDatabase";
    private static ResponceDatabase surveyDatabase;

    public static ResponceDatabase getInstance(Context context) {
        if (surveyDatabase == null) {
            synchronized (LOCK) {
                surveyDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        ResponceDatabase.class,
                        ResponceDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return surveyDatabase;
    }

    public abstract ResponceDao responceDao();
}
