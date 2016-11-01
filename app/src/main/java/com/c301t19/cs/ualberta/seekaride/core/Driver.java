package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Driver extends User {

    private ArrayList<Request> searchedRequests;
    private ArrayList<Request> acceptedRequests;

    public void searchRequestsByKeyword(ArrayList<String> keywords, int radius) {
        // search requests and store in searchedRequests
    }

    public void searchRequestsByLocation() {
        // search requests and store in searchedRequests
    }

    public void acceptRequest() {
        // accept a request and add to acceptedRequests
    }

    public void unacceptRequest() {
        // unaccept a request and remove from acceptedRequests
    }

    public void acceptPayment() {

    }
}
