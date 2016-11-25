package com.c301t19.cs.ualberta.seekaride.mock;

import android.annotation.SuppressLint;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Master Chief on 2016-11-25.
 */
@SuppressLint("ParcelCreator")
public class MockGeoPoint extends GeoPoint {

    public MockGeoPoint(double lat, double lon) {
        super(lat, lon);
    }
}
