package com.example.mobilefinal.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ObservationDao {
    @Insert
    void insert(Observation observation);
    @Update
    void update(Observation observation);
    @Delete
    void delete(Observation observation);
    @Query("SELECT * FROM Observation WHERE trip_id = :tripId ORDER BY observation_time DESC")
    LiveData<List<Observation>> getObservationsForTrip(int tripId);
    @Query("DELETE FROM Observation WHERE trip_id = :tripId")
    void deleteAllObservationsForTrip(int tripId);
    @Query("SELECT * FROM Observation WHERE id = :id")
    Observation getObservationById(int id);
}