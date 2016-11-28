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
     * Delete a request.
     * @param request
     */
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

    /**
     * Edit a request by passing the edited version to this function.
     * @param edited The edited request.
     */
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
     * Accepts a driver's offer.
     * @param request The request to be accepted.
     * @param driverProfile The profile of the driver to accept.
     * @return true if successful
     */
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
     * Complete a request by paying the driver.
     * @see ElasticsearchController
     * @param request The request to be completed.
     */
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
     * Get the list of open Requests.
     *
     * @return ArrayList of open Requests.
     */
    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    /**
     * Executes all commands stored in riderCommands.
     */
    public void executeRiderCommands() {
        for (int i = 0; i < riderCommands.size(); i++) {
            riderCommands.get(i).execute();
        }
        riderCommands = new ArrayList<RiderCommand>();
    }

    /**
     * Checks if a driver has accepted any of the rider's open requests.
     * @return true if an open request has been accepted
     */
    public boolean driverHasAccepted() {
        for (int i = 0; i < openRequests.size(); i++) {
            if (!openRequests.get(i).getAcceptedDriverProfiles().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
