package com.c301t19.cs.ualberta.seekaride.mock;

import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.c301t19.cs.ualberta.seekaride.core.RiderCommand;
import com.c301t19.cs.ualberta.seekaride.core.User;

import java.util.ArrayList;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockRider extends Rider {

    private static MockRider ourInstance = null;

    protected MockRider(MockProfile p) {
        super(p);
    }

    /**
     * Gets instance.
     *
     * @return the instance of Rider.
     */
    public static MockRider getInstance() {
        return ourInstance;
    }

    /**
     * Creates an instance of Rider using a user's Profile. Meant to be called during login.
     * Must be called before other Rider methods are used.
     *
     * @param p The user's profile.
     */
    public static void instantiate(MockProfile p) {
        ourInstance = new MockRider(p);
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
    public MockRequest makeRequest(String description, Location startPoint, Location destination, double price, String requestId) {
            MockRequest q = new MockRequest(description, startPoint, destination,price,this.getProfile(),getProfile().getId(), requestId);
            openRequests.add(q);
            MockElasticsearchController.AddRequestTask(q);
            return q;
    }

    @Override
    public void deleteRequest(Request request) {
            openRequests.remove(request);
            MockElasticsearchController.DeleteRequestTask(request);
    }

    @Override
    public void editRequest(Request edited) {
           MockElasticsearchController.DeleteRequestTask(edited);
            MockElasticsearchController.AddRequestTask(edited);

    }

    /**
     * Make payment.
     *
     * @param indexR the index r
     */
    @Override
    public void makePayment(Request request) {
            if (request.didDriverReceivePay()) {
                MockElasticsearchController.DeleteRequestTask(request);
            } else {
                Request edited = new Request(request);
                edited.riderPay();
                MockElasticsearchController.DeleteRequestTask(request);
                MockElasticsearchController.AddRequestTask(edited);
            }

    }

    /**
     * Retrieves the Rider's current list of open Requests from the database and updates openRequests.
     */
    @Override
    public void updateOpenRequests() {
            openRequests = MockElasticsearchController.GetRequestsTask(
                    ElasticsearchController.RequestField.RIDERID, getProfile().getId());

    }

    @Override
    public void leaveReview(Review review) {
        MockElasticsearchController.AddReviewTask(review);
    }
}
