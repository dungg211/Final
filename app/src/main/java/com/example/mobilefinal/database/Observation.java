package com.example.mobilefinal.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Observation",
        // Thiết lập Khóa ngoại (Foreign Key)
        foreignKeys = @ForeignKey(entity = Trip.class,
                parentColumns = "id",
                childColumns = "trip_id",
                onDelete = ForeignKey.CASCADE)) // Nếu xóa Trip, Observation cũng bị xóa
public class Observation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Khóa ngoại liên kết tới Bảng "Trip"
    @ColumnInfo(name = "trip_id")
    public int trip_id;

    // Nội dung quan sát (Bắt buộc)
    @ColumnInfo(name = "observation_text")
    public String observationText;

    // Thời gian quan sát (Bắt buộc)
    @ColumnInfo(name = "observation_time")
    public String observationTime;

    // Bình luận (Không bắt buộc)
    @ColumnInfo(name = "comments")
    public String comments;

    // Constructor
    public Observation(int trip_id, String observationText, String observationTime, String comments) {
        this.trip_id = trip_id;
        this.observationText = observationText;
        this.observationTime = observationTime;
        this.comments = comments;
    }
}