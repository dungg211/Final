package com.example.mobilefinal.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Observation",
        foreignKeys = @ForeignKey(entity = Trip.class,
                parentColumns = "id",
                childColumns = "trip_id",
                onDelete = ForeignKey.CASCADE))
public class Observation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "trip_id")
    public int trip_id;
    @ColumnInfo(name = "observation_text")
    public String observationText;
    @ColumnInfo(name = "observation_time")
    public String observationTime;
    @ColumnInfo(name = "comments")
    public String comments;
    public Observation(int trip_id, String observationText, String observationTime, String comments) {
        this.trip_id = trip_id;
        this.observationText = observationText;
        this.observationTime = observationTime;
        this.comments = comments;
    }
}