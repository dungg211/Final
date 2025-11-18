package com.example.mobilefinal.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Insert
    void insert(Trip trip);
    @Update
    void update(Trip trip);
    @Delete
    void delete(Trip trip);
    @Query("DELETE FROM Trip")
    void deleteAllTrips();
    @Query("SELECT * FROM Trip ORDER BY date DESC")
    LiveData<List<Trip>> getAllTrips();
    @Query("SELECT * FROM Trip WHERE id = :tripId")
    Trip getTripById(int tripId);
    @Query("SELECT * FROM Trip WHERE name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    LiveData<List<Trip>> searchTrips(String query);
}