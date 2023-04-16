package com.lordierclaw.testapplication;

import java.io.Serializable;

public class LocationModel implements Serializable {
    double latitude = 0;
    double longitude = 0;

    public LocationModel() {

    }
    public LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
