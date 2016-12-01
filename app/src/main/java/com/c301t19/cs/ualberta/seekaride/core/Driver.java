package com.c301t19.cs.ualberta.seekaride.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * A Singleton controller class for Driver activities.
 */
public class Driver extends User {

    protected ArrayList<Request> searchedRequests; //never update it, after we accept a request, until the request ends
    protected ArrayList<Request> acceptedRequests;

    private static Driver ourInstance = null;

    protected ArrayList<DriverCommand> driverCommands;

    protected Driver(Profile profile) {
        super(profile);
        searchedRequests = new ArrayList<Request>();
        acceptedRequests = new ArrayList<Request>();
        driverCommands = new ArrayList<DriverCommand>();
    }

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
     * @param profile The user's profile.
     */
    public static void instantiate(Profile profile) {
        ourInstance = new Driver(profile);
    }

    /**
     * Searches the database for Requests and stores them in searchedRequests.
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
     * Searches the database for Requests and stores them in searchedRequests.
     * @param location the search Location
     * @param radius   the radius
     */
    public void searchRequestsByLocation(Location location, double radius) {
        // search requests and store in searchedRequests
        ElasticsearchController.SearchRequestsByLocationTask searchTask =
                new ElasticsearchController.SearchRequestsByLocationTask(location, radius);
        searchTask.execute();
        try {
            searchedRequests = searchTask.get();
        }
        catch (Exception e) {

        }
    }

    /**
     * Allows the Driver to accept a Request and add it to acceptedRequests.
     * @param request The Request to be accepted.
     */
    public void acceptRequest(Request request) {
        // accept a request and add to acceptedRequests
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(request);
            DriverCommand command = new DriverCommand(DriverCommand.CommandType.ACCEPT_REQUEST, params);
            driverCommands.add(command);

        } else {
            Request oldRequest = new Request(request);
            oldRequest.driverAccepted(getProfile());
            acceptedRequests.add(oldRequest);
            String reqId = oldRequest.getId();

            ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
            ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(oldRequest, reqId);
            deleteRequestTask.execute();
            addRequestTask.execute();
        }
    }

    @Deprecated
    public void removeAcceptedRequest(int index) {
        // remove a request and remove from acceptedRequests
        removeAcceptedRequest(acceptedRequests.get(index));
    }

    /**
     * Allows the Driver to decline a Request they previously accepted and remove it from acceptedRequests.
     * @param request the Request to be removed
     */
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
     * @return ArrayList of accepted Requests.
     */
    public ArrayList<Request> getAcceptedRequests() {
        return acceptedRequests;
    }


    /**
     * Allows the driver to complete a request and receive payment.
     * @param request The request to be completed
     */
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
    @Deprecated
    public boolean isConfirmed(int index) {
        return acceptedRequests.get(index).isRiderConfirmed();
    }

    /**
     * Get the current list of search results.
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

    /**
     * Updates the list of accepted requests from the server. Also checks to see if a rider has accepted this
     * driver on any of their requests. If so, acceptedRequest will be set to the accepted request and the request
     * can be started.
     */
    public void updateAcceptedRequests() {
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            return;
        }
        else {
            if (getProfile() == null) {
                Log.i("profile", "is null");
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
        }

        Request acceptedRequest = riderHasAccepted();
        Log.i("accepted null", ((Boolean)(acceptedRequest==null)).toString());
        if (acceptedRequest != null && acceptedRequest.getAcceptedDriverProfile()!=null && acceptedRequest.getAcceptedDriverProfile().equals(getProfile())) {
            setRequestInProgress(acceptedRequest);
            Request current;
            while (!acceptedRequests.isEmpty())
            {
                current = acceptedRequests.get(0);
                if (current.equals(acceptedRequest)) {
                    acceptedRequests.remove(current);
                }
                else {
                    removeAcceptedRequest(current);
                }
            }
            acceptedRequests = new ArrayList<Request>();
            acceptedRequests.add(acceptedRequest);
        }
        else {
            setRequestInProgress(null);
        }
    }

    /**
     * Check if a rider has accepted any of the driver's offers.
     * @return the Request that the rider has accepted, or null if none.
     */
    public Request riderHasAccepted() {
        for (int i = 0; i < acceptedRequests.size(); i++) {
            if (!acceptedRequests.get(i).getWaitingForRider()&&
                    acceptedRequests.get(i).getAcceptedDriverProfile()!=null &&
                    acceptedRequests.get(i).getAcceptedDriverProfile().equals(getProfile()) ) {
                return acceptedRequests.get(i);
            }
        }
        return null;
    }

    /**
     * Get a Request in acceptedRequests with a given id. Null if it doesn't exist
     * @param requestId id of the request
     * @return a Request with the given id, or null if nonexistent
     */
    public Request getRequest(String requestId) {
        for (int i = 0; i < acceptedRequests.size(); i++) {
            if (acceptedRequests.get(i).getId().equals(requestId)) {
                return acceptedRequests.get(i);
            }
        }
        Log.i("NULL" ,"REQUEST");
        return null;
    }

    /**
     * Removes from searchedRequests requests that don't meet a threshold price
     * @param price The minimum price to filter by
     */
    public void filterRequestsByPrice(double price) {
        ArrayList<Request> newSearchedRequests = new ArrayList<Request>();
        for (int i = 0; i < searchedRequests.size(); i++) {
            if (searchedRequests.get(i).getPrice() >= price) {
                newSearchedRequests.add(searchedRequests.get(i));
            }
        }
        searchedRequests = newSearchedRequests;
    }

    /**
     * Removes from searchedRequests requests that don't meet a threshold price per km travelled
     * @param price The minimum price to filter by
     */
    public void filterRequestsByPricePerKm(double price) {
        ArrayList<Request> newSearchedRequests = new ArrayList<Request>();
        for (int i = 0; i < searchedRequests.size(); i++) {
            if (searchedRequests.get(i).getPricePerKm() >= price) {
                newSearchedRequests.add(searchedRequests.get(i));
            }
        }
        searchedRequests = newSearchedRequests;
    }
}
