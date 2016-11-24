package com.c301t19.cs.ualberta.seekaride.core;

import org.osmdroid.util.GeoPoint;

import java.text.DecimalFormat;

/**
 * Stores a Geolocation.
 * <p/>
 * Issues:
 */
public class Location {

    private String address;

    private GeoPoint geoLocation;

    //private double lat;
    //private double lon;

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
        //lat = geoLocation.getLatitude();
        //lon = geoLocation.getLongitude();
    }

    /**
     * Gets geo location.
     *
     * @return the geo location
     */
    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public String calculateFare(Location loc2) {
        GeoPoint geo1 = this.geoLocation;
        GeoPoint geo2 = loc2.getGeoLocation();
        float distanceInMeters = geo1.distanceTo(geo2);
        float distanceInKm = distanceInMeters/1000;
        double costPerKm = 1.48;
        //http://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount
        DecimalFormat dFormat = new DecimalFormat("#.##");
        double cost = (double) distanceInKm * costPerKm;
        return ("$" + dFormat.format(cost));
    }
}
