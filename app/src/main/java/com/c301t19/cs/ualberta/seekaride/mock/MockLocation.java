package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.Location;

import org.osmdroid.util.GeoPoint;

import java.text.DecimalFormat;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockLocation extends Location {

    /**
     * Instantiates a new Location.
     *
     * @param a the a
     */
    public MockLocation(String a) {
        super(a);
    }

    /**
     * Sets geo location.
     *
     * @param geoLocation the geo location
     */
    public void setGeoLocation(double latitude, double longitude) {
        this.geoLocation = new MockGeoPoint(latitude,longitude);
    }

    @Override
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
