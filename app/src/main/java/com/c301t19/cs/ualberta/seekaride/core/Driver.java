package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

/**
 * A Singleton controller class for Driver activities.
 */
public class Driver extends User {

    private ArrayList<Request> searchedRequests; //never update it, after we accept a request, until the request ends
    private ArrayList<Request> acceptedRequests;
    private Request acceptedRequest;

    private static Driver ourInstance = null;

    private ArrayList<DriverCommand> driverCommands;

    private Driver(Profile p) {
        super(p);
        searchedRequests = new ArrayList<Request>();
        acceptedRequests = new ArrayList<Request>();
        acceptedRequest = null;
        driverCommands = new ArrayList<DriverCommand>();
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

    /**
     * Gets instance.
     *
     * @return the instance of Driver.
     */
    public static Driver getInstance() {
        return ourInstance;
    }

    /**
     * Creates an instance of Driver using a user's Profile. Meant to be called during login.
     * Must be called before other Driver methods are used.
     *
     * @param p The user's profile.
     */
    public static void instantiate(Profile p) {
        ourInstance = new Driver(p);
    }

    /**
     * Searches the database for Requests and stores them in searchedRequests.
     * Issues: Still need to implement a search radius.
     *
     * @param keywords The user-entered string of keywords.
     * @param radius   The user-entered search radius.
     */
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

    /**
     * To be implemented.
     *
     * @param location the location
     * @param radius   the radius
     */
    public void searchRequestsByLocation(Location location, String radius) {
        // search requests and store in searchedRequests
    }

    /**
     * Allows the Driver to accept a Request and add it to acceptedRequests.
     *
     * @param request The Request to be accepted.
     */
    public void acceptRequest(Request request) {
        // accept a request and add to acceptedRequests
        Request oldRequest = new Request(request);
        oldRequest.driverAccepted(getProfile());
        acceptedRequests.add(oldRequest);
        String reqId = oldRequest.getId();

        ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
        ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(oldRequest, reqId);
        deleteRequestTask.execute();
        addRequestTask.execute();
    }

    /**
     * Allows the Driver to decline a Request they previously accepted and remove it from acceptedRequests.
     * Issues: may need to change index parameter to the Request itself
     *
     * @param index The request's position in acceptedRequests.
     */
    public void removeAcceptedRequest(int index) {
        // remove a request and remove from acceptedRequests
        removeAcceptedRequest(acceptedRequests.get(index));
    }

    public void removeAcceptedRequest(Request request) {
        Request oldRequest = new Request(request);
        oldRequest.driverDeclined(getProfile());
        acceptedRequests.remove(request);
        String reqId = oldRequest.getId();

        ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
        ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(oldRequest, reqId);
        deleteRequestTask.execute();
        addRequestTask.execute();
    }


    /**
     * Get the list of accepted Requests.
     *
     * @return ArrayList of accepted Requests.
     */
    public ArrayList<Request> getAcceptedRequests() {
        return acceptedRequests;
    }

    /**
     * Receive payment.
     */
    public void receivePayment(int index) {
        if(acceptedRequests.get(index).didRiderPay()) {
            acceptedRequests.get(index).driverReceivePay();
        }
    }

    public void receivePayment(Request request) {
        if (request.didRiderPay()) {
            ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
            deleteRequestTask.execute();
        }
        else {
            Request edited = new Request(request);
            edited.driverReceivePay();
            ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
            ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(edited, edited.getId());
            deleteRequestTask.execute();
            addRequestTask.execute();
        }
    }

    /**
     * Is confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isConfirmed(int index) {
        return acceptedRequests.get(index).isRiderConfirmed();
    }

    /**
     * Get the current list of search results.
     *
     * @return ArrayList of searched Requests.
     */
    public ArrayList<Request> getSearchedRequests() {
        return searchedRequests;
    }

    public void executeDriverCommands() {
        for (int i = 0; i < driverCommands.size(); i++) {
            driverCommands.get(i).execute();
        }
        driverCommands = new ArrayList<DriverCommand>();
    }

    public void updateAcceptedRequests() {
        if (getProfile() == null) {
            acceptedRequests = new ArrayList<Request>();
            return;
        }
        /*ArrayList<String> requestIds = new ArrayList<String>();
        for (int i = 0; i < acceptedRequests.size(); i++) {
            requestIds.add(acceptedRequests.get(i).getId());
        }*/

        ElasticsearchController.GetRequestsTask getRequestsTask;
        ArrayList<Request> reqs;
        acceptedRequests = new ArrayList<Request>();
        getRequestsTask = new ElasticsearchController.GetRequestsTask(
                ElasticsearchController.RequestField.DRIVERID, getProfile().getId());
        getRequestsTask.execute();
        try {
            reqs = getRequestsTask.get();
            if (reqs != null && !reqs.isEmpty()) {
                acceptedRequests = reqs;
            }
        }
        catch (Exception e) {

        }
        pollForRiderAcceptances();
    }

    private void pollForRiderAcceptances() {
        for (int i = 0; i < acceptedRequests.size(); i++) {
            if (!acceptedRequests.get(i).getWaitingForRider()) {
                // send notification
            }
        }
    }

    public Request getRequest(String requestId) {
        for (int i = 0; i < acceptedRequests.size(); i++) {
            if (acceptedRequests.get(i).getId().equals(requestId)) {
                return acceptedRequests.get(i);
            }
        }
        return null;
    }

    public void filterRequestsByPrice(double price) {
        ArrayList<Request> newSearchedRequests = new ArrayList<Request>();
        for (int i = 0; i < searchedRequests.size(); i++) {
            if (searchedRequests.get(i).getPrice() >= price) {
                newSearchedRequests.add(searchedRequests.get(i));
            }
        }
        searchedRequests = newSearchedRequests;
    }
}
