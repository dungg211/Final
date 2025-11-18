package com.example.mobilefinal.database; // (Package của bạn)

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Trip.class, Observation.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TripDao tripDao();
    public abstract ObservationDao observationDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "M-Hike_Database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}