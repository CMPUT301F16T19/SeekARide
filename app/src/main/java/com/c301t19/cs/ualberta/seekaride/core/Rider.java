package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Rider extends User {

    //Request currentRequest;
    private ArrayList<Request> openRequests;

    public Rider (Profile userProfile) {
        super(userProfile );
        openRequests = new ArrayList<Request>();
    }

    public Request getRequest(int index) {
        return openRequests.get(index);
    }

    public Request makeRequest(String description, Location startPoint, Location destination, float price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
        Request q = new Request(description, startPoint, destination,price,this.getProfile());
        openRequests.add(q);
        return q;
    }



    public void deleteRequest(int index) {
        openRequests.remove(index);
    }

    public void acceptDriverOffer(int indexR, int indexD) {
        openRequests.get(indexR).riderAccept(indexD);
    }

    public void makePayment(int indexR) {
        openRequests.get(indexR).riderPay();
    }


    public void updateOpenRequests(ArrayList<Request> updateRequests) {
        openRequests = updateRequests;
    }

    public boolean hasRequests() {
        if (openRequests.isEmpty()){
            return false;
        }else{
            return true;
        }
    }


    public void completeRequest(int index) {
        openRequests.get(index).complete();
    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }
}
