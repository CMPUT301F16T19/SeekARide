package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Holds the data associated with a Request. Requests are stored in an external database (Elasticsearch).
 */
public class Request {

    @JestId
    private String id;
    private String riderId;

    private Date requestTime;
    private String description;
    private Location start;
    private Location destination;
    //private String riderName;
    private ArrayList<Profile> acceptedDriverProfiles;
    private double price;

    private Boolean waitingForDriver;
    private Boolean completion;
    private Boolean riderPaid;
    private Boolean driverIsPaid;

    private Profile driverProfile;
    private Profile riderProfile;

    public Request(String descrip, Location st, Location dest,
                    double pr,Profile riderProfile, String riderId) {
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
        riderPaid = false;
        driverIsPaid = false;
        driverProfile = null;

        this.riderId = riderId;
    }

    public void driverAccepted(Profile driverProfile) {

        acceptedDriverProfiles.add(driverProfile);
        //ElasticsearchController.EditRequestTask editRequestTask = new ElasticsearchController.EditRequestTask();
       // editRequestTask.execute();
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

    public double getPrice() {
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

    public void riderPay() {
        riderPaid = true;
    }

    public boolean isPaid(){
        return riderPaid;
    }

    public boolean isGotPayment(){
        return driverIsPaid;
    }

    public void driverReceivePayment() {
        driverIsPaid = true;
    }

    public boolean isRiderConfirmed() {
        if (driverProfile == null){
            return false;
        }else{
            return true;
        }
    }

    public String getId() { return id; }

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        if (((Request)(obj)).getId().equals(id)) {
            return true;
        }
        return false;
    }
}
