package com.example.roomlistar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double latitude;
    public double longitude;
    // Constructor, getters y setters

    public LocationEntity() {
    }

    public LocationEntity(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

