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
    private double lat;
    private double lon;

    private Boolean waitingForRider;
    private Profile acceptedDriverProfile;
    private Boolean riderPaid;
    private Boolean driverIsPaid;

    private boolean inProgress;
    private Boolean completion;

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
        inProgress = false;
        lat = st.getGeoLocation().getLatitude();
        lon = st.getGeoLocation().getLongitude();

        waitingForRider = true;
        this.riderProfile = riderProfile;
        completion = false;
        riderPaid = false;
        driverIsPaid = false;
        acceptedDriverProfile = null;

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
        inProgress = r.isInProgress();

        waitingForRider = r.getWaitingForRider();
        completion = r.isCompleted();
        riderPaid = r.didRiderPay();
        driverIsPaid = r.didDriverReceivePay();

        acceptedDriverProfile = r.getAcceptedDriverProfile();
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

    public boolean didDriverAccept(Profile driverProfile) {
        if (acceptedDriverProfiles.contains(driverProfile)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Is waiting for driver boolean.
     *
     * @return the boolean
     */
    public boolean getWaitingForRider(){return waitingForRider;}

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
    public boolean riderAccept(int index) {
        waitingForRider = false;
        acceptedDriverProfile = acceptedDriverProfiles.get(index);
        return true;
    }

    public boolean riderAccept(Profile driverProfile) {
        if (!acceptedDriverProfiles.contains(driverProfile))
        {
            return false;
        }
        waitingForRider = false;
        acceptedDriverProfile = driverProfile;
        return true;
    }

    /**
     * Gets driver profile.
     *
     * @return the driver profile
     */
    public Profile getAcceptedDriverProfile() {
        return acceptedDriverProfile;
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
    public boolean didRiderPay(){
        return riderPaid;
    }

    /**
     * Is got payment boolean.
     *
     * @return the boolean
     */
    public boolean didDriverReceivePay(){
        return driverIsPaid;
    }

    /**
     * Driver receive payment.
     */
    public void driverReceivePay() {
        driverIsPaid = true;
    }

    /**
     * Is rider confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isRiderConfirmed() {
        if (acceptedDriverProfile == null){
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
        //removed if statement, replaced if with a return.
        return ((Request) (obj)).getId().equals(id);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public boolean isInProgress() {
        return inProgress;
    }

    @Deprecated
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
