package com.example.mobilefinal.database; // (Package của bạn)

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// === BƯỚC 1: THÊM Observation.class VÀ TĂNG version LÊN 2 ===
@Database(entities = {Trip.class, Observation.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // === BƯỚC 2: KHAI BÁO CẢ HAI DAO ===
    public abstract TripDao tripDao();
    public abstract ObservationDao observationDao(); // (Dòng này của bạn đã đúng)

    // Singleton pattern
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "M-Hike_Database")

                            // === BƯỚC 3: THÊM DÒNG NÀY (Rất quan trọng) ===
                            // Dòng này bảo Room hãy xóa database cũ (v1)
                            // và tạo database mới (v2)
                            .fallbackToDestructiveMigration()

                            .build();
                }
            }
        }
        return INSTANCE;
    }
}