package com.example.mobilefinal.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Trip")
public class Trip implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "parking_available")
    public boolean parkingAvailable;
    @ColumnInfo(name = "length_km")
    public double lengthKm;
    @ColumnInfo(name = "difficulty_level")
    public String difficultyLevel;
    @ColumnInfo(name = "movement_permit")
    public boolean movementPermit;
    @ColumnInfo(name = "participant_count")
    public int participantCount;
    public Trip(String name, String location, String date, boolean parkingAvailable,
                double lengthKm, String difficultyLevel, boolean movementPermit,
                int participantCount) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.lengthKm = lengthKm;
        this.difficultyLevel = difficultyLevel;
        this.movementPermit = movementPermit;
        this.participantCount = participantCount;
    }
}