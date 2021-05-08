package com.example.ass3.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class PainRecord {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "pain_intensity_level")
    @NonNull
    public int painLevel;
    @ColumnInfo(name = "pain_location")
    @NonNull
    public String painLocation;
    @ColumnInfo(name = "mood_level")
    @NonNull
    public int moodLevel;
    @ColumnInfo(name = "step_taken")
    @NonNull
    public int stepTaken;
    @NonNull
    public String recordDate;
    @NonNull
    public double temperature;
    @NonNull
    public double humidity;
    @NonNull
    public double pressure;
    @NonNull
    public String username;

    public PainRecord() {
    }

    public PainRecord(int uid, int painLevel, @NonNull String painLocation, int moodLevel, int stepTaken, @NonNull String recordDate, double temperature, double humidity, double pressure, @NonNull String username) {
        this.uid = uid;
        this.painLevel = painLevel;
        this.painLocation = painLocation;
        this.moodLevel = moodLevel;
        this.stepTaken = stepTaken;
        this.recordDate = recordDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.username = username;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public void setPainLocation(@NonNull String painLocation) {
        this.painLocation = painLocation;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public void setStepTaken(int stepTaken) {
        this.stepTaken = stepTaken;
    }

    public void setDate(@NonNull String recordDate) {
        this.recordDate = recordDate;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }
}