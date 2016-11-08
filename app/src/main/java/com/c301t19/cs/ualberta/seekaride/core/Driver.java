package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Driver extends User {

    private ArrayList<Request> searchedRequests; //never update it, after we accept a request, until the request ends
    private Request acceptedRequest;


    // singleton
    // pass in the username to create a new driver
    /*
    private static Driver ourInstance = null;
    public static Driver getInstance(String username) {
        ourInstance = new Driver(username);
        return ourInstance;
    }
    public static Driver getInstance() {
        return ourInstance;
    }
    private Driver(String username) {

    }
    */

    public Driver(Profile profile) {
        super(profile);
        this.searchedRequests = new ArrayList<Request>();
        acceptedRequest = null;
    }

    public Driver(Profile profile, ArrayList<Request> searchedRequests) {
        super(profile);
        this.searchedRequests = searchedRequests;
        acceptedRequest = null;
    }

    public void searchRequestsByKeyword(ArrayList<String> keywords, int radius) {
        // search requests and store in searchedRequests
    }

    public void searchRequestsByLocation(Location location) {
        // search requests and store in searchedRequests
    }

    //
    public void acceptRequest(Request testRequest) {
        // accept a request and add to acceptedRequests
        testRequest.driverAccepted(this.getProfile());
        acceptedRequest = testRequest;
    }

    public void removeSearchedRequest(int index) {
        // remove a request and remove from searchedRequests
        searchedRequests.remove(index);
    }


    public Request getCurrentRequest() {
        return acceptedRequest;
    }

    public void receivePayment() {
        if(acceptedRequest.isPaid()) {
            acceptedRequest.driverReceivePayment();
        }
    }
}
