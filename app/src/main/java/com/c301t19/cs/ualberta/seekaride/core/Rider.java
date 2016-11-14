package com.c301t19.cs.ualberta.seekaride.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Rider extends User {

    //Request currentRequest;
    private ArrayList<Request> openRequests;

    // singleton
    private static Rider ourInstance = null;
    public static Rider getInstance() {
        return ourInstance;
    }
    private Rider(Profile p) {
        super(p);
        openRequests = new ArrayList<Request>();
    }
    public static void instantiate(Profile p) {
        ourInstance = new Rider(p);
    }

    /*
    public Rider (Profile userProfile) {
        super(userProfile );
        openRequests = new ArrayList<Request>();
    }
    */

    public Request getRequest(int index) {
        return openRequests.get(index);
    }

    public Request makeRequest(String description, Location startPoint, Location destination, double price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
        Request q = new Request(description, startPoint, destination,price,this.getProfile(),getProfile().getId());
        openRequests.add(q);
        ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(q);
        addRequestTask.execute();
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
