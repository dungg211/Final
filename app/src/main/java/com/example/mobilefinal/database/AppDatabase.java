package com.example.mobilefinal.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Declare the entities (tables) and the version
@Database(entities = {Trip.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Declare the DAO
    public abstract TripDao tripDao();

    // Singleton pattern (ensures only one database instance)
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "M-Hike_Database") // Database file name
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}