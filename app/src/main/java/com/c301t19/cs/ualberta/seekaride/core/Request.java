package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mc on 16/10/13.
 */
public class Request {

    private Date requestTime;
    private String description;
    private Location start;
    private Location destination;
    private String riderName;
    private ArrayList<String> acceptedDriverNames;
    private float price;

    public Request(String descrip, Location st, Location dest,
                   String name, float pr) {
        requestTime = new Date();
        description = descrip;
        start = st;
        destination = dest;
        riderName = name;
        acceptedDriverNames = new ArrayList<String>();
        price = pr;
    }

    public void driverAccepted(String driverName) {
        acceptedDriverNames.add(driverName);
    }

    public void driverUnaccepted(String driverName) {
        acceptedDriverNames.remove(driverName);
    }

    public void riderAccepted(String driverName) {
        
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public String getDescription() {
        return description;
    }

    public Location getStart() {
        return start;
    }

    public Location getDestination() {
        return destination;
    }

    public String getRiderName() {
        return riderName;
    }

    public ArrayList<String> getAcceptedDriverNames() {
        return acceptedDriverNames;
    }

    public float getPrice() {
        return price;
    }
}
