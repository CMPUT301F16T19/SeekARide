package com.c301t19.cs.ualberta.seekaride.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * A Singleton controller class for Rider activities.
 */
public class Rider extends User {

    //Request currentRequest;
    private ArrayList<Request> openRequests;

    private static Rider ourInstance = null;

    private Rider(Profile p) {
        super(p);
        openRequests = new ArrayList<Request>();
    }

    /*
    public Rider (Profile userProfile) {
        super(userProfile );
        openRequests = new ArrayList<Request>();
    }
    */

    /**
     * @return the instance of Rider.
     */
    public static Rider getInstance() {
        return ourInstance;
    }

    /**
     * Creates an instance of Rider using a user's Profile. Meant to be called during login.
     * Must be called before other Rider methods are used.
     * @param p The user's profile.
     */
    public static void instantiate(Profile p) {
        ourInstance = new Rider(p);
    }

    /**
     * Get one of the Rider's open Requests at the given index.
     * Issues: may need to change index parameter to the Request itself
     * @param index The request's position in openRequests.
     */
    public Request getRequest(int index) {
        return openRequests.get(index);
    }

    /**
     * Makes a new Request and sends it to the database.
     * @param description User-entered description of the Request.
     * @param startPoint User-selected starting Location.
     * @param destination User-selected ending Location.
     * @param price User-entered price.
     * @return The Request just made.
     */
    public Request makeRequest(String description, Location startPoint, Location destination, double price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
        Request q = new Request(description, startPoint, destination,price,this.getProfile(),getProfile().getId());
        openRequests.add(q);
        ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(q);
        addRequestTask.execute();
        return q;
    }

    /**
     * Remove one of the User's open Requests.
     * Issues: may need to change index parameter to the Request itself
     * @param index The request's position in openRequests.
     */
    public void deleteRequest(int index) {
        openRequests.remove(index);
    }

    /**
     * Accept a Driver's offer.
     * @param indexR
     * @param indexD
     */
    public void acceptDriverOffer(int indexR, int indexD) {
        openRequests.get(indexR).riderAccept(indexD);
    }

    public void makePayment(int indexR) {
        openRequests.get(indexR).riderPay();
    }

    /**
     * Retrieves the Rider's current list of open Requests from the database and updates openRequests.
     */
    public void updateOpenRequests() {
        if (getProfile() == null) {
            openRequests = new ArrayList<Request>();
            return;
        }
        ElasticsearchController.GetRequestsTask getRequestsTask = new ElasticsearchController.GetRequestsTask(
                ElasticsearchController.RequestField.RIDERID, getProfile().getId());
        getRequestsTask.execute();
        try {
            ArrayList<Request> requests = getRequestsTask.get();
            if (requests == null) {
                openRequests = new ArrayList<Request>();
            }
            else {
                openRequests = requests;
            }
        }
        catch (Exception e) {

        }
    }

    /**
     * Check if the Rider has any open Requests.
     * @return true if the Rider has open Requests.
     */
    public boolean hasRequests() {
        if (openRequests.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Complete a request.
     * Issues: may need to change index parameter to the Request itself
     * @param index The request's position in openRequests.
     */
    public void completeRequest(int index) {
        openRequests.get(index).complete();
    }

    /**
     * Get the list of open Requests.
     * @return ArrayList of open Requests.
     */
    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }
}
