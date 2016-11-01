package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Rider extends User {

    Request currentRequest;
    private ArrayList<Request> openRequests;

    public Rider (Profile userProfile) {
        super(userProfile );
        openRequests = new ArrayList<Request>();
    }

    public Request getCurrentRequests() {
        return currentRequest;
    }

    public void makeRequest(String description, Location startPoint, Location destination, float price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
        String name = this.getProfile().getUsername();
        currentRequest = new Request(description, startPoint, destination,price,this.getProfile());

    }



    public void deleteRequest(int index) {
        currentRequest = null;
        openRequests.remove(index);
    }

    public void acceptDriverOffer(int index) {
        currentRequest.riderAccept(index);

    }

    public void makePayment() {

    }

    public void updateRequest(Request updateRequest) {
        currentRequest = updateRequest;
    }

    public boolean hasCurrentRequest() {
        if (currentRequest == null){
            return false;
        }else{
            return true;
        }
    }


    public void completeCurrentRequest() {
        currentRequest.complete();
    }
}
