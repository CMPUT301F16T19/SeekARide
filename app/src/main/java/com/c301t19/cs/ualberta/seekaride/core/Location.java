package com.c301t19.cs.ualberta.seekaride.core;

import org.osmdroid.util.GeoPoint;

/**
 * Stores a Geolocation.
 * <p/>
 * Issues:
 */
public class Location {

    private String address;

    private GeoPoint geoLocation;

    /**
     * Instantiates a new Location.
     *
     * @param a the a
     */
    public Location(String a) {
        address = a;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param a the a
     */
    public void setAddress(String a) {
        address = a;
    }

    /**
     * Sets geo location.
     *
     * @param geoLocation the geo location
     */
    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * Gets geo location.
     *
     * @return the geo location
     */
    public GeoPoint getGeoLocation() {
        return geoLocation;
    }
}
