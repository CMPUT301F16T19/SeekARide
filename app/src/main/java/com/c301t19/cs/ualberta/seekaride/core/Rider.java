package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Rider extends User {

    private ArrayList<Request> openRequests;

    public Rider () {

    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    public void makeRequest(String description, Location destination, float price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
    }

    public float getRecommendedPrice(Location start, Location end) {
        return 0;
    }

    public void deleteRequest(int index) {
        openRequests.remove(index);
    }

    public void acceptDriverOffer(int index) {

    }

    public void makePayment(Request req) {

    }
}
