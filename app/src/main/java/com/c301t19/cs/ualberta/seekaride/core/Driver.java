package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mc on 16/10/13.
 */
public class Driver extends User {

    private ArrayList<Request> searchedRequests; //never update it, after we accept a request, until the request ends
    private ArrayList<Request> acceptedRequests;
    private Request acceptedRequest;

    // singleton
    private static Driver ourInstance = null;
    public static Driver getInstance() {
        return ourInstance;
    }
    private Driver(Profile p) {
        super(p);
        searchedRequests = new ArrayList<Request>();
        acceptedRequests = new ArrayList<Request>();
        acceptedRequest = null;
    }
    public static void instantiate(Profile p) {
        ourInstance = new Driver(p);
    }

    /*
    public Driver(Profile profile) {
        super(profile);
        this.searchedRequests = new ArrayList<Request>();
        acceptedRequest = null;
    }

    public Driver(Profile profile, ArrayList<Request> searchedRequests) {
        super(profile);
        this.searchedRequests = searchedRequests;
        acceptedRequest = null;
    }*/

    public void searchRequestsByKeyword(String keywords, String radius) {
        // search requests and store in searchedRequests
        // taken from http://stackoverflow.com/questions/4674850/converting-a-sentence-string-to-a-string-array-of-words-in-java
        // 2016-11-12, 4:53 PM, author helloworld922
        // and from http://stackoverflow.com/questions/157944/create-arraylist-from-array, author Tom
        //ArrayList<String> words = new ArrayList<String>(Arrays.asList(keywords.replace(".", "").replace(",", "").replace("?", "").replace("!","").split(" ")));
        ElasticsearchController.SearchRequestsByKeywordTask searchTask = new ElasticsearchController.SearchRequestsByKeywordTask(keywords);
        searchTask.execute();
        try {
            searchedRequests = searchTask.get();
        }
        catch (Exception e) {

        }
    }

    public void searchRequestsByLocation(Location location, String radius) {
        // search requests and store in searchedRequests
    }

    public void acceptRequest(Request request) {
        // accept a request and add to acceptedRequests
        request.driverAccepted(getProfile());
        acceptedRequests.add(request);
    }

    public void removeSearchedRequest(int index) {
        // remove a request and remove from searchedRequests
        searchedRequests.remove(index);
    }


    public ArrayList<Request> getAcceptedRequests() {
        return acceptedRequests;
    }

    public void receivePayment() {
        if(acceptedRequest.isPaid()) {
            acceptedRequest.driverReceivePayment();
        }
    }

    public boolean isConfirmed() {
        return acceptedRequest.isRiderConfirmed();
    }

    public ArrayList<Request> getSearchedRequests() {
        return searchedRequests;
    }
}
