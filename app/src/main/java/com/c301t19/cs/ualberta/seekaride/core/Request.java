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
    //private String riderName;
    private ArrayList<Profile> acceptedDriverProfiles;
    private float price;

    private Boolean waitingForDriver;
    private Profile riderProfile;
    private Boolean completion;
    private Profile driverProfile;

    public Request(String descrip, Location st, Location dest,
                    float pr,Profile riderProfile) {
        requestTime = new Date();
        description = descrip;
        start = st;
        destination = dest;
        //riderName = name;
        acceptedDriverProfiles = new ArrayList<Profile>();
        price = pr;

        waitingForDriver = true;
        this.riderProfile = riderProfile;
        completion = false;
    }

    public void driverAccepted(Profile driverProfile) {
        acceptedDriverProfiles.add(driverProfile);
    }

//    public void driverUnaccepted(Profile driverName) {
//        acceptedDriverProfile.remove(driverName);
//    }

    public void riderAccepted(String driverName) {
        
    }

    public boolean isWaitingForDriver(){return waitingForDriver;}

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

    public Profile getRiderProfile() {
        return riderProfile;
    }

    public ArrayList<Profile> getAcceptedDriverProfiles() {
        return acceptedDriverProfiles;
    }

    public float getPrice() {
        return price;
    }


    public void complete() {
        completion = true;
    }


    public boolean isCompleted() {
        return completion;
    }

    public void riderAccept(int index) {
        waitingForDriver = false;
        driverProfile = acceptedDriverProfiles.get(index);
    }

    public Profile getDriverProfile() {
        return driverProfile;
    }
}
