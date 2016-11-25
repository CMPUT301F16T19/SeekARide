package com.c301t19.cs.ualberta.seekaride.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * A Singleton controller class for Rider activities.
 */
public class Rider extends User {

    protected ArrayList<Request> openRequests;

    private static Rider ourInstance = null;

    protected ArrayList<RiderCommand> riderCommands;

    protected Rider(Profile p) {
        super(p);
        openRequests = new ArrayList<Request>();
        riderCommands = new ArrayList<RiderCommand>();
    }

    /**
     * Gets instance.
     *
     * @return the instance of Rider.
     */
    public static Rider getInstance() {
        return ourInstance;
    }

    /**
     * Creates an instance of Rider using a user's Profile. Meant to be called during login.
     * Must be called before other Rider methods are used.
     *
     * @param p The user's profile.
     */
    public static void instantiate(Profile p) {
        ourInstance = new Rider(p);
    }

    /**
     * Get one of the Rider's open Requests at the given index.
     * Issues: may need to change index parameter to the Request itself
     *
     * @param index The request's position in openRequests.
     * @return the request
     */
    public Request getRequest(int index) {
        return openRequests.get(index);
    }

    public Request getRequest(String requestId) {
        for (int i = 0; i < openRequests.size(); i++) {
            if (openRequests.get(i).getId().equals(requestId)) {
                return openRequests.get(i);
            }
        }
        return null;
    }

    /**
     * Makes a new Request and sends it to the database.
     *
     * @param description User-entered description of the Request.
     * @param startPoint  User-selected starting Location.
     * @param destination User-selected ending Location.
     * @param price       User-entered price.
     * @return The Request just made.
     */
    public Request makeRequest(String description, Location startPoint, Location destination, double price) {
        // make a request and add it to openRequests
        // send request to Elasticsearch
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(description);
            params.add(startPoint);
            params.add(destination);
            params.add(price);
            RiderCommand command = new RiderCommand(RiderCommand.CommandType.MAKE_REQUEST, params);
            riderCommands.add(command);
            return null;
        }
        else {
            Request q = new Request(description, startPoint, destination,price,this.getProfile(),getProfile().getId());
            openRequests.add(q);
            ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(q);
            addRequestTask.execute();

            return q;
        }
    }

    /**
     * Remove one of the User's open Requests.
     * Issues: may need to change index parameter to the Request itself
     *
     * @param index The request's position in openRequests.
     */
    @Deprecated
    public void deleteRequest(int index) {

        deleteRequest(openRequests.get(index));

    }

    public void deleteRequest(Request request) {
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(request);
            RiderCommand command = new RiderCommand(RiderCommand.CommandType.DELETE_REQUEST, params);
            riderCommands.add(command);
        }else {
            openRequests.remove(request);
            ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
            deleteRequestTask.execute();
        }
    }

    public void editRequest(Request edited) {
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(edited);
            RiderCommand command = new RiderCommand(RiderCommand.CommandType.EDIT_REQUEST, params);
            riderCommands.add(command);
        }else {
            ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(edited.getId());
            deleteRequestTask.execute();
            ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(edited, edited.getId());
            addRequestTask.execute();
        }
    }
    /**
     * Accept a Driver's offer.
     *
     * @param indexR the index r
     * @param indexD the index d
     */
    @Deprecated
    public void acceptDriverOffer(int indexR, int indexD) {
        openRequests.get(indexR).riderAccept(indexD);
    }

    public boolean acceptDriverOffer(Request request, Profile driverProfile) {
        if (Driver.getInstance().getRequestInProgress() != null) {
            Log.i("request in progress", "can't accept two requests at once");
            return false;
        }
        if (getRequestInProgress() != null) {
            Log.i("request in progress", "can't accept two requests at once");
            return false;
        }
        if (!request.riderAccept(driverProfile)) {
            return false;
        }
        Rider.getInstance().editRequest(request);
        setRequestInProgress(request);
        return true;
    }

    /**
     * Make payment.
     *
     * @param indexR the index r
     */
    @Deprecated
    public void makePayment(int indexR) {

        openRequests.get(indexR).riderPay();
    }

    public void makePayment(Request request) {
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(request);
            RiderCommand command = new RiderCommand(RiderCommand.CommandType.MAKE_PAYMENT, params);
            riderCommands.add(command);
        }else {
            if (request.didDriverReceivePay()) {
                ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
                deleteRequestTask.execute();
            } else {
                Request edited = new Request(request);
                edited.riderPay();
                ElasticsearchController.DeleteRequestTask deleteRequestTask = new ElasticsearchController.DeleteRequestTask(request);
                ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(edited, edited.getId());
                deleteRequestTask.execute();
                addRequestTask.execute();
            }
        }
    }

    /**
     * Retrieves the Rider's current list of open Requests from the database and updates openRequests.
     */
    public void updateOpenRequests() {
        //This if statement does nothing by design.
        if (NetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            return;
        }
        else {
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
                } else {
                    openRequests = requests;
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * Check if the Rider has any open Requests.
     *
     * @return true if the Rider has open Requests.
     */
    @Deprecated
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
     *
     * @param index The request's position in openRequests.
     */
    @Deprecated
    public void completeRequest(int index) {
        openRequests.get(index).complete();
    }

    /**
     * Get the list of open Requests.
     *
     * @return ArrayList of open Requests.
     */
    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    public void executeRiderCommands() {
        for (int i = 0; i < riderCommands.size(); i++) {
            riderCommands.get(i).execute();
        }
        riderCommands = new ArrayList<RiderCommand>();
    }

    public boolean driverHasAccepted() {
        for (int i = 0; i < openRequests.size(); i++) {
            if (!openRequests.get(i).getAcceptedDriverProfiles().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
