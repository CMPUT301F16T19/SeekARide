package com.c301t19.cs.ualberta.seekaride.core;

import org.osmdroid.util.GeoPoint;

import java.text.DecimalFormat;

/**
 * Stores a Geolocation.
 */
public class Location {

    private String address;

    protected GeoPoint geoLocation;

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
     * @param address the a
     */
    public void setAddress(String address) {
        this.address = address;
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

    /**
     * calculate the Fare
     * @param location
     * @return string
     */
    public String calculateFare(Location location) {
        double distanceInKm = this.calculateDistanceInKm(location);
        double costPerKm = 1.48;
        //http://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount
        DecimalFormat dFormat = new DecimalFormat("#.##");
        double cost = distanceInKm * costPerKm;
        return ("$" + dFormat.format(cost));
    }

    /**
     *
     * @param location
     * @return distanceInKm(double)
     */
    public double calculateDistanceInKm(Location location) {
        GeoPoint geo1 = this.geoLocation;
        GeoPoint geo2 = location.getGeoLocation();
        float distanceInMeters = geo1.distanceTo(geo2);
        float distanceInKm = distanceInMeters / 1000;
        return (double) distanceInKm;
    }
}
