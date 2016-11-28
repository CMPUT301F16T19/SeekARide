package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Holds the data associated with a Request. Requests are stored in an external database (Elasticsearch).
 */
public class Request {

    @JestId
    protected String id;
    @Deprecated
    private String riderId;

    private Date requestTime;
    private String description;

    private Location start;
    private Location destination;
    //private String riderName;
    private ArrayList<Profile> acceptedDriverProfiles;
    private double price;

    private double pricePerKm;
    private double lat;
    private double lon;

    private boolean waitingForRider;
    private Profile acceptedDriverProfile;
    private boolean riderPaid;
    private boolean driverIsPaid;

    private boolean inProgress;
    private boolean completion;

    private Profile riderProfile;

    /**
     * Instantiates a new Request.
     *
     * @param description     a description of the request
     * @param start           the start Location
     * @param destination     the destination Location
     * @param price           the price
     * @param riderProfile    the rider's profile
     * @param riderId         the rider's id
     */
    public Request(String description, Location start, Location destination,
                    double price,Profile riderProfile, String riderId) {
        requestTime = new Date();
        this.description = description;
        this.start = start;
        this.destination = destination;
        //riderName = name;
        acceptedDriverProfiles = new ArrayList<Profile>();
        this.price = price;
        pricePerKm = price / start.calculateDistanceInKm(destination);
        inProgress = false;
        lat = start.getGeoLocation().getLatitude();
        lon = start.getGeoLocation().getLongitude();


        waitingForRider = true;
        this.riderProfile = riderProfile;
        completion = false;
        riderPaid = false;
        driverIsPaid = false;
        acceptedDriverProfile = null;

        this.riderId = riderId;
    }

    /**
     * Copy constructor of request
     * @param request Request to be copied
     */
    public Request(Request request) {
        id = request.getId();
        riderId = request.getRiderProfile().getId();

        requestTime = request.getRequestTime();
        description = request.getDescription();
        start = request.getStart();
        destination = request.getDestination();
        acceptedDriverProfiles = request.getAcceptedDriverProfiles();
        price = request.getPrice();
        pricePerKm = request.getPricePerKm();
        inProgress = request.isInProgress();
        lat = request.getLat();
        lon = request.getLon();

        waitingForRider = request.getWaitingForRider();
        completion = request.isCompleted();
        riderPaid = request.didRiderPay();
        driverIsPaid = request.didDriverReceivePay();

        acceptedDriverProfile = request.getAcceptedDriverProfile();
        riderProfile = request.getRiderProfile();
    }

    /**
     * Call when a driver accepts a request.
     *
     * @param driverProfile the profile of the driver to be accepted.
     */
    public void driverAccepted(Profile driverProfile) {

        acceptedDriverProfiles.add(driverProfile);
        //ElasticsearchController.EditRequestTask editRequestTask = new ElasticsearchController.EditRequestTask();
       // editRequestTask.execute();
    }

    /**
     * Call when a driver declines a request.
     * @param driverProfile the profile of the declining driver.
     */
    public void driverDeclined(Profile driverProfile) {

        acceptedDriverProfiles.remove(driverProfile);
        //ElasticsearchController.EditRequestTask editRequestTask = new ElasticsearchController.EditRequestTask();
        // editRequestTask.execute();
    }

    /**
     * See if a driver has accepted the request
     * @param driverProfile The profile of the driver in question
     * @return true if this driver has accepted
     */
    public boolean didDriverAccept(Profile driverProfile) {
        if (acceptedDriverProfiles.contains(driverProfile)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if drivers are waiting for the rider to accept an offer.
     *
     * @return true if drivers are waiting for the rider to accept an offer.
     */
    public boolean getWaitingForRider(){return waitingForRider;}

    /**
     * Gets request time.
     *
     * @return the request Date
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
     * Get the start Location
     *
     * @return start Location
     */
    public Location getStart() {
        return start;
    }

    /**
     * Get the destination.
     *
     * @return destination Location
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Gets latitude of start location for elastic search
     * @return latitude of start location
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets longitude of start location for elastic search
     * @return longitude of start location
     */
    public double getLon() {
        return lon;
    }

    /**
     * Gets the profile of the rider who made the request.
     *
     * @return the rider profile
     */
    public Profile getRiderProfile() {
        return riderProfile;
    }

    /**
     * Get a list of drivers who have accepted this request.
     *
     * @return the accepted driver profiles
     */
    public ArrayList<Profile> getAcceptedDriverProfiles() {
        return acceptedDriverProfiles;
    }

    /**
     * Gets the price of the request.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets price per km of the request
     *
     * @return price per km
     */
    public double getPricePerKm() {
        return pricePerKm;
    }

    /**
     * Completes the request.
     */
    public void complete() {
        completion = true;
    }


    /**
     * Checks if the request is complete.
     *
     * @return true if the request is complete
     */
    public boolean isCompleted() {
        return completion;
    }

    /**
     * Rider accept.
     *
     * @param index the index
     */
    @Deprecated
    public boolean riderAccept(int index) {
        waitingForRider = false;
        acceptedDriverProfile = acceptedDriverProfiles.get(index);
        return true;
    }

    /**
     * Allows the rider accept one of the driver's offers.
     * @param driverProfile the profile of the driver to be accepted
     * @return boolean true if the acceptance was successful. false if the driver has not accepted this request.
     */
    public boolean riderAccept(Profile driverProfile) {
        if (!acceptedDriverProfiles.contains(driverProfile))
        {
            return false;
        }
        waitingForRider = false;
        acceptedDriverProfile = driverProfile;
        acceptedDriverProfiles = new ArrayList<Profile>();
        acceptedDriverProfiles.add(driverProfile);
        return true;
    }

    /**
     * Gets the profile of the driver chosen by the rider.
     *
     * @return the driver profile
     */
    public Profile getAcceptedDriverProfile() {
        return acceptedDriverProfile;
    }

    /**
     * The rider pays to complete the request.
     */
    public void riderPay() {
        riderPaid = true;
        driverIsPaid = true;
    }

    /**
     * Check if the rider has paid.
     *
     * @return true if the rider has paid
     */
    public boolean didRiderPay(){
        return riderPaid;
    }

    /**
     * Check if the driver has received their pay
     *
     * @return true if the driver has received their pay
     */
    public boolean didDriverReceivePay(){
        return driverIsPaid;
    }

    /**
     * The driver receives their pay to complete the request.
     */
    public void driverReceivePay() {
        riderPaid = true;
        driverIsPaid = true;
    }

    /**
     * Check if the rider has accepted a driver's offer.
     *
     * @return true if the rider has accepted a driver's offer.
     */
    public boolean isRiderConfirmed() {
        if (acceptedDriverProfile == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Gets the unique ID of the request.
     *
     * @return the id
     */
    public String getId() { return id; }

    /**
     * to see 2 object are equals or not
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        //removed if statement, replaced if with a return.
        return ((Request) (obj)).getId().equals(id);
    }

    /**
     * Set the description
     * @param description User-entered description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the price
     * @param price Price offered by rider
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * See if the request is in progress
     * @return true if the request is in progress
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * set InProgress
     * @param inProgress
     */
    @Deprecated
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    /**
     * Set start location
     * @param start Location
     */
    public void setStart(Location start) {
        this.start = start;
    }

    /**
     * Set Destination
     * @param destination Location
     */
    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
