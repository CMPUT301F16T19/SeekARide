package com.c301t19.cs.ualberta.seekaride.mock;

import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.DriverCommand;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.User;

import java.util.ArrayList;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockDriver extends Driver {

    private static MockDriver ourInstance = null;

    private MockDriver(MockProfile p) {
        super(p);
    }

    /**
     * Gets instance.
     *
     * @return the instance of Driver.
     */
    public static MockDriver getInstance() {
        return ourInstance;
    }

    /**
     * Creates an instance of Driver using a user's Profile. Meant to be called during login.
     * Must be called before other Driver methods are used.
     *
     * @param p The user's profile.
     */
    public static void instantiate(MockProfile p) {
        ourInstance = new MockDriver(p);
    }

    /**
     * Searches the database for Requests and stores them in searchedRequests.
     * Issues: Still need to implement a search radius.
     *
     * @param keywords The user-entered string of keywords.
     * @param radius   The user-entered search radius.
     */

    @Override
    public void leaveReview(Review review) {
        MockElasticsearchController.AddReviewTask(review);
    }

    @Override
    public void searchRequestsByKeyword(String keywords, String radius) {
        searchedRequests = MockElasticsearchController.SearchRequestsByKeywordTask(keywords);
    }

    /**
     * To be implemented.
     *
     * @param location the location
     * @param radius   the radius
     */
    @Override
    public void searchRequestsByLocation(Location location, double radius) {
        // search requests and store in searchedRequests
        searchedRequests = MockElasticsearchController.SearchRequestsByLocationTask(location, radius);
    }

    /**
     * Allows the Driver to accept a Request and add it to acceptedRequests.
     *
     * @param request The Request to be accepted.
     */
    @Override
    public void acceptRequest(Request request) {
        if (MockNetworkManager.getInstance().getConnectivityStatus() == NetworkManager.Connectivity.NONE) {
            Log.i("internet", "NONE");
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(request);
            MockDriverCommand command = new MockDriverCommand(DriverCommand.CommandType.ACCEPT_REQUEST, params);
            driverCommands.add(command);
        }
        else {
            Request oldRequest = new Request(request);
            oldRequest.driverAccepted(getProfile());
            acceptedRequests.add(oldRequest);
            MockElasticsearchController.DeleteRequestTask(request);
            MockElasticsearchController.AddRequestTask(oldRequest);
        }
    }
    public void receivePayment(int index) {
        if(acceptedRequests.get(index).didRiderPay()) {
            acceptedRequests.get(index).driverReceivePay();
        }
    }

    /**
     * Allows the Driver to decline a Request they previously accepted and remove it from acceptedRequests.
     * Issues: may need to change index parameter to the Request itself
     *
     * @param request The request's position in acceptedRequests.
     */
    @Override
    public void removeAcceptedRequest(Request request) {
        Request oldRequest = new Request(request);
        oldRequest.driverDeclined(getProfile());
        acceptedRequests.remove(request);

        MockElasticsearchController.DeleteRequestTask(request);
        MockElasticsearchController.AddRequestTask(oldRequest);
    }

    @Override
    public void receivePayment(Request request) {
        if (request.didRiderPay()) {
            MockElasticsearchController.DeleteRequestTask(request);
        }
        else {
            Request edited = new Request(request);
            edited.driverReceivePay();
            MockElasticsearchController.DeleteRequestTask(request);
            MockElasticsearchController.AddRequestTask(edited);
        }
    }

    @Override
    public void updateAcceptedRequests() {
        acceptedRequests = MockElasticsearchController.GetRequestsTask(
                ElasticsearchController.RequestField.DRIVERID, getProfile().getId());

        Request acceptedRequest = riderHasAccepted();
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
    }
}
