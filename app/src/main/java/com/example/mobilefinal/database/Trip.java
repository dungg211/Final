package com.example.mobilefinal.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Trip") // Name of the table
public class Trip {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Name
    @ColumnInfo(name = "name")
    public String name;

    // Location
    @ColumnInfo(name = "location")
    public String location;

    // Date - Saved as String
    @ColumnInfo(name = "date")
    public String date;

    // Parking Available (boolean)
    @ColumnInfo(name = "parking_available")
    public boolean parkingAvailable;

    // Length (km) - Use double for km
    @ColumnInfo(name = "length_km")
    public double lengthKm;

    // Level of difficulty
    @ColumnInfo(name = "difficulty_level")
    public String difficultyLevel;

    // Movement permit (boolean)
    @ColumnInfo(name = "movement_permit")
    public boolean movementPermit;

    // Number of participants
    @ColumnInfo(name = "participant_count")
    public int participantCount;

    // Room uses this constructor to create objects
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