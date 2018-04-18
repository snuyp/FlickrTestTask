package com.example.dima.flickrtesttask.model;

/**
 * Created by Dima on 18.04.2018.
 */

public class Location {
    private String longitude;

    private String latitude;

    private String accuracy;

    public Location() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
