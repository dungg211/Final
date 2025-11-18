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

    // Add a new trip
    @Insert
    void insert(Trip trip);

    // Update a trip
    @Update
    void update(Trip trip);

    // Delete a trip
    @Delete
    void delete(Trip trip);

    // Delete all trips
    @Query("DELETE FROM Trip")
    void deleteAllTrips();

    // Get all trips (ordered by date)
    // Using LiveData automatically updates your UI
    @Query("SELECT * FROM Trip ORDER BY date DESC")
    LiveData<List<Trip>> getAllTrips();

    // Get 1 trip by its ID (for editing)
    @Query("SELECT * FROM Trip WHERE id = :tripId")
    Trip getTripById(int tripId);
    @Query("SELECT * FROM Trip WHERE name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    LiveData<List<Trip>> searchTrips(String query);
}