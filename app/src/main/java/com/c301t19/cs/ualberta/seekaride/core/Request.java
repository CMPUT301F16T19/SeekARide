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

    /**
     * Instantiates a new Request.
     *
     * @param descrip      the descrip
     * @param st           the st
     * @param dest         the dest
     * @param pr           the pr
     * @param riderProfile the rider profile
     * @param riderId      the rider id
     */
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

    public Request(Request r) {
        id = r.getId();
        riderId = r.getRiderProfile().getId();

        requestTime = r.getRequestTime();
        description = r.getDescription();
        start = r.getStart();
        destination = r.getDestination();
        acceptedDriverProfiles = r.getAcceptedDriverProfiles();
        price = r.getPrice();

        waitingForDriver = r.isWaitingForDriver();
        completion = r.isCompleted();
        riderPaid = r.isPaid();
        driverIsPaid = r.isGotPayment();

        driverProfile = r.getDriverProfile();
        riderProfile = r.getRiderProfile();
    }

    /**
     * Driver accepted.
     *
     * @param driverProfile the driver profile
     */
    public void driverAccepted(Profile driverProfile) {

        acceptedDriverProfiles.add(driverProfile);
        //ElasticsearchController.EditRequestTask editRequestTask = new ElasticsearchController.EditRequestTask();
       // editRequestTask.execute();
    }

    public void driverDeclined(Profile driverProfile) {

        acceptedDriverProfiles.remove(driverProfile);
        //ElasticsearchController.EditRequestTask editRequestTask = new ElasticsearchController.EditRequestTask();
        // editRequestTask.execute();
    }

//    public void driverUnaccepted(Profile driverName) {
//        acceptedDriverProfile.remove(driverName);
//    }

    /**
     * Rider accepted.
     *
     * @param driverName the driver name
     */
    public void riderAccepted(String driverName) {
        
    }

    /**
     * Is waiting for driver boolean.
     *
     * @return the boolean
     */
    public boolean isWaitingForDriver(){return waitingForDriver;}

    /**
     * Gets request time.
     *
     * @return the request time
     */
    public Date getRequestTime() {
        return requestTime;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public Location getStart() {
        return start;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Gets rider profile.
     *
     * @return the rider profile
     */
    public Profile getRiderProfile() {
        return riderProfile;
    }

    /**
     * Gets accepted driver profiles.
     *
     * @return the accepted driver profiles
     */
    public ArrayList<Profile> getAcceptedDriverProfiles() {
        return acceptedDriverProfiles;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }


    /**
     * Complete.
     */
    public void complete() {
        completion = true;
    }


    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    public boolean isCompleted() {
        return completion;
    }

    /**
     * Rider accept.
     *
     * @param index the index
     */
    public void riderAccept(int index) {
        waitingForDriver = false;
        driverProfile = acceptedDriverProfiles.get(index);
    }

    /**
     * Gets driver profile.
     *
     * @return the driver profile
     */
    public Profile getDriverProfile() {
        return driverProfile;
    }

    /**
     * Rider pay.
     */
    public void riderPay() {
        riderPaid = true;
    }

    /**
     * Is paid boolean.
     *
     * @return the boolean
     */
    public boolean isPaid(){
        return riderPaid;
    }

    /**
     * Is got payment boolean.
     *
     * @return the boolean
     */
    public boolean isGotPayment(){
        return driverIsPaid;
    }

    /**
     * Driver receive payment.
     */
    public void driverReceivePayment() {
        driverIsPaid = true;
    }

    /**
     * Is rider confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isRiderConfirmed() {
        if (driverProfile == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
