package com.c301t19.cs.ualberta.seekaride.mock;

import android.os.AsyncTask;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockElasticsearchController extends ElasticsearchController {

    private static ArrayList<Request> requests;
    private static ArrayList<Profile> profiles;
    private static ArrayList<Review> reviews;

    /**
     * Adds a Profile to Elasticsearch.
     */
    public static boolean AddUserTask(Profile profile) {
        profiles.add(profile);
        return Boolean.TRUE;
    }

    /**
     * Retrieves a Profile from Elasticsearch.
     * <p/>
     */
    public static Profile GetUserTask(UserField userField, String keyword) {
            switch (userField) {
                case NAME:
                    for (int i = 0; i < profiles.size(); i++) {
                        if (profiles.get(i).getUsername().equals(keyword)) {
                            return profiles.get(i);
                        }
                    }
                    return null;
                case ID:
                    for (int i = 0; i < profiles.size(); i++) {
                        if (profiles.get(i).getId().equals(keyword)) {
                            return profiles.get(i);
                        }
                    }
                    return null;
                default:
                    return null;
            }
    }

    public static boolean DeleteUserTask(Profile profile) {
        profiles.remove(profile);
        return Boolean.TRUE;
    }

    /**
     * Add a Request to Elasticsearch.
     */
    public static boolean AddRequestTask (Request request) {
        /*for (int i = 0; i < requests.size(); i++) {
            if (request.getId().equals(requests.get(i).getId())) {
                requests.remove(requests.get(i));
                break;
            }
        }*/
        requests.add(request);
        return true;
    }

    /**
     * Retrieves an ArrayList of Requests from Elasticsearch. Currently allows you to search by RiderID, DriverID, and RequestID
     */
    public static ArrayList<Request> GetRequestsTask(RequestField requestField, String keyword){
        ArrayList<Request> result = new ArrayList<Request>();
            switch (requestField) {
                case ID:
                    for (int i = 0; i < requests.size(); i++) {
                        if (requests.get(i).getId().equals(keyword)) {
                            result.add(requests.get(i));
                        }
                    }
                    return result;
                case RIDERID:
                    for (int i = 0; i < requests.size(); i++) {
                        if (requests.get(i).getRiderProfile().getId().equals(keyword)) {
                            result.add(requests.get(i));
                        }
                    }
                    return result;
                case DRIVERID:
                    ArrayList<Profile> drivers;
                    for (int i = 0; i < requests.size(); i++) {
                        drivers = requests.get(i).getAcceptedDriverProfiles();
                        for (int j = 0; j < drivers.size(); j++) {
                            if (drivers.get(i).getId().equals(keyword)) {
                                result.add(requests.get(i));
                            }
                        }
                    }
                    return result;
                default:
                    return null;
            }
        }

    /**
     * The type Search requests by location task.
     */
    public static ArrayList<Request> SearchRequestsByLocationTask(Location location, double radius) {
        ArrayList<Request> result = new ArrayList<Request>();
        result = filterRequestsInProgress(result);
        return result;
    }

    /**
     * Retrieves an ArrayList of Requests from Elasticsearch.
     * <p/>
     * Issues: Currently only allows you to search by keyword, which only targets a request's description.
     */
    public static ArrayList<Request> SearchRequestsByKeywordTask(String keywords) {
        ArrayList<Request> result = new ArrayList<Request>();
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getDescription().equals(keywords)) {
                result.add(requests.get(i));
            }
        }
        result = filterRequestsInProgress(result);
        return result;
    }

    /**
     * The type Delete request task.
     */
    public static boolean DeleteRequestTask(Request request) {
        requests.remove(request);
        return true;
    }

    public static boolean AddReviewTask (Review review) {

        reviews.add(review);
        return true;
    }

    public static ArrayList<Review> GetReviewsTask (String userID) {
        ArrayList<Review> result = new ArrayList<Review>();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getUserID().equals(userID)) {
                result.add(reviews.get(i));
            }
        }
        return result;
    }
}
