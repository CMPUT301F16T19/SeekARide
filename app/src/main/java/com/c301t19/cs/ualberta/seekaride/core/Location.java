package com.c301t19.cs.ualberta.seekaride.core;

import org.osmdroid.util.GeoPoint;

/**
 * Stores a Geolocation.
 *
 * Issues:
 */
public class Location {

    private String address;

    private GeoPoint geoLocation;

    public Location(String a) {
        address = a;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        address = a;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }
}
