package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

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
 * Allows asynchronous interaction with the Elasticsearch database.
 */
public class ElasticsearchController {

    public static final String INDEX = "t19seekaride2";

    /**
     * Indicates a field in a userProfile's Profile for search tasks.
     */
    public enum UserField {
        NAME, ID }

    /**
     * Indicates a field in a Request for search tasks.
     */
    public enum RequestField {
        RIDERID, DRIVERID, ID }

    private static JestDroidClient client;

    /**
     * Adds a Profile to Elasticsearch. Returns true if successful.
     */
    public static class AddUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile userProfile;
        private String id;

        /**
         * @param profile The profile to add.
         */
        public AddUserTask(Profile profile) {
            super();
            userProfile = profile;
            id = null;
        }

        /**
         * @param profile The profile to add.
         * @param id Allows a user with a specific id to be added. Used for editing accounts.
         */
        public AddUserTask(Profile profile, String id) {
            super();
            userProfile = profile;
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index;
            if (id == null)
            {
                Log.i("making", "id");
                index = new Index.Builder(userProfile).index(INDEX).type("userProfile").build();
            }
            else
            {
                index = new Index.Builder(userProfile).index(INDEX).type("userProfile").id(id).build();
            }
            try {
                Log.i("getting", "id");
                client.execute(index);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    /**
     * Retrieves a Profile from Elasticsearch, given either an id or username.
     */
    public static class GetUserTask extends AsyncTask<Void, Void, Profile> {

        private UserField userField;
        private String keyword;

        /**
         * @param userField The field to search by (id or name)
         * @param keyword The id or username
         */
        public GetUserTask(UserField userField, String keyword) {
            super();
            this.userField = userField;
            this.keyword = keyword;
        }

        @Override
        protected Profile doInBackground(Void... params) {
            verifySettings();
            switch (userField) {
                case NAME:
                    String query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"username\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    Search search = new Search.Builder(query)
                            .addIndex(INDEX)
                            .addType("userProfile")
                            .build();
                    List<Profile> profiles = null;
                    try {
                        SearchResult result = client.execute(search);
                        profiles = result.getSourceAsObjectList(Profile.class);
                    }
                    catch (Exception e) {
                        Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                    }
                    if (profiles == null || profiles.size() < 1) {
                        return null;
                    }
                    else {
                        for (int i = 0; i < profiles.size(); i++) {
                            if (profiles.get(i).getUsername().equals(keyword)) {
                                return profiles.get(i);
                            }
                        }
                        Log.i("fail", "mega fail");
                        return null;
                    }
                case ID:
                    Get get = new Get.Builder(INDEX, keyword).type("userProfile").build();
                    JestResult result = null;
                    try {
                        result = client.execute(get);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result.getSourceAsObject(Profile.class);
                default:
                    return null;
            }
        }
    }

    /**
     * Deletes a user from Elasticsearch. Returns true if successful.
     */
    public static class DeleteUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile user;

        /**
         * @param profile User profile to delete.
         */
        public DeleteUserTask(Profile profile) {
            super();
            user = profile;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Delete delete  = new Delete.Builder(user.getId()).index(INDEX).type("userProfile").build();
            try {
                client.execute(delete);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    /**
     * Add a Request to Elasticsearch. Returns true if successful.
     */
    public static class AddRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;
        private String id;

        /**
         * @param request The request to be added.
         */
        public AddRequestTask(Request request) {
            super();
            this.request = request;
            id = null;
        }

        /**
         * @param request The request to be added.
         * @param id Allows a request to be added with a specific id. Used to edit a request.
         */
        public AddRequestTask(Request request, String id) {
            super();
            this.request = request;
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index;
            if (id == null) {
                index = new Index.Builder(request).index(INDEX).type("request").build();
            }
            else {
                index = new Index.Builder(request).index(INDEX).type("request").id(id).build();
            }
            try {
                client.execute(index);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    /**
     * Retrieves an ArrayList of Requests from Elasticsearch. Allows you to search by RiderID, DriverID, and RequestID.
     * Used to get specific requests. If searching based on fuzzy queries, use a Search task instead
     * @see SearchRequestsByKeywordTask
     * @see SearchRequestsByLocationTask
     */
    public static class GetRequestsTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private RequestField requestField;
        private String keyword;

        /**
         * @param requestField The request field to be searched by (rider id, driver id, or request id)
         * @param keyword The search term
         */
        public GetRequestsTask(RequestField requestField, String keyword) {
            super();
            this.requestField = requestField;
            this.keyword = keyword;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();
            String query;
            switch (requestField) {
                case ID:
                    Get get = new Get.Builder(INDEX, keyword).type("request").build();
                    JestResult result = null;
                    try {
                        result = client.execute(get);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Request> res = new ArrayList<Request>();
                    res.add(result.getSourceAsObject(Request.class));
                    return res;
                case RIDERID:
                    query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"riderId\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    break;
                case DRIVERID:
                    query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"acceptedDriverProfiles.id\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    break;
                default:
                    return null;
            }
            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType("request")
                    .build();
            List<Request> requests = new ArrayList<Request>();
            try {
                SearchResult result = client.execute(search);
                requests = result.getSourceAsObjectList(Request.class);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return (ArrayList<Request>) requests;
        }
    }

    /**
     * Gets a list of Requests based on a Location query.
     * @see SearchRequestsByKeywordTask
     */
    public static class SearchRequestsByLocationTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private Location location;
        private double radius;

        /**
         * @param location the location
         * @param radius the search radius
         */
        public SearchRequestsByLocationTask(Location location, double radius) {
            super();
            this.location = location;
            this.radius = radius;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();

            // GeoPoint querying did not work, so here is my calculated version

            // 111km per degree lat or lon
            double radiusToDegree = radius / 111;
            String lowerLat = String.valueOf(location.getGeoLocation().getLatitude()
                                                - radiusToDegree);
            String upperLat = String.valueOf(location.getGeoLocation().getLatitude()
                                                + radiusToDegree);
            String lowerLon = String.valueOf(location.getGeoLocation().getLongitude()
                                                - radiusToDegree);
            String upperLon = String.valueOf(location.getGeoLocation().getLongitude()
                                                + radiusToDegree);

            // Elasticsearch made it really "easy" to form this query
            // https://www.elastic.co/guide/en/elasticsearch/guide/current/bool-query.html
            // https://www.elastic.co/guide/en/elasticsearch/reference/5.0/query-dsl-range-query.html
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"bool\" : {\n" +
                    "           \"must\" : {\n" +
                    "               \"range\" : {\n" +
                    "                   \"lat\" : {\n" +
                    "                       \"gte\" :" + lowerLat + ",\n" +
                    "                       \"lte\" :" + upperLat + "\n" +
                    "                   }\n" +
                    "               }},\n" +
                    "           \"must\" : {\n" +
                    "               \"range\" : {\n" +
                    "                   \"lon\" : {\n" +
                    "                       \"gte\" :" + lowerLon + ",\n" +
                    "                       \"lte\" :" + upperLon + "\n" +
                    "                   }\n" +
                    "               }\n" +
                    "           }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType("request")
                    .build();
            List<Request> requests = new ArrayList<Request>();
            try {
                SearchResult result = client.execute(search);
                requests = result.getSourceAsObjectList(Request.class);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            //return (ArrayList<Request>) requests;
            ArrayList<Request> filteredRequests = filterRequestsInProgress((ArrayList<Request>) requests);
            return filteredRequests;
        }
    }

    /**
     * Gets a list of Requests based on a description query.
     * @see SearchRequestsByLocationTask
     */
    public static class SearchRequestsByKeywordTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private String keywords;

        /**
         * @param keyword Search term
         */
        public SearchRequestsByKeywordTask(String keyword) {
            super();
            this.keywords = keyword;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"description\" : \"" + keywords + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType("request")
                    .build();
            List<Request> requests = new ArrayList<Request>();
            try {
                SearchResult result = client.execute(search);
                requests = result.getSourceAsObjectList(Request.class);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            ArrayList<Request> filteredRequests = filterRequestsInProgress((ArrayList<Request>) requests);
            return filteredRequests;
        }
    }

    /**
     * Delete a request. Requests can be deleted by passing them in directly or their id. Returns true if successful.
     */
    public static class DeleteRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;
        private String id;

        /**
         * @param request The request to be deleted.
         */
        public DeleteRequestTask(Request request) {
            super();
            this.request = request;
            id = null;
        }

        /**
         * @param id The id of the request to be deleted
         */
        public DeleteRequestTask(String id) {
            super();
            request = null;
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Delete delete;
            if (id == null) {
                delete  = new Delete.Builder(request.getId()).index(INDEX).type("request").build();
            }
            else {
                delete  = new Delete.Builder(id).index(INDEX).type("request").build();
            }
            try {
                client.execute(delete);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    /**
     * Adds a review. Returns true if successful.
     */
    public static class AddReviewTask extends AsyncTask<Void, Void, Boolean> {

        private Review review;

        /**
         * @param review The review to be added.
         */
        public AddReviewTask(Review review) {
            super();
            this.review = review;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index = new Index.Builder(review).index(INDEX).type("review").build();
            try {
                client.execute(index);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    /**
     * Gets a list of reviews for a given user.
     */
    public static class GetReviewsTask extends AsyncTask<Void, Void, ArrayList<Review>> {

        private String userID;

        /**
         * @param userID id of the user to get reviews for
         */
        public GetReviewsTask(String userID) {
            super();
            this.userID = userID;
        }

        @Override
        protected ArrayList<Review> doInBackground(Void... params) {
            verifySettings();
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"userID\" : \"" + userID + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType("review")
                    .build();
            List<Review> reviews = new ArrayList<Review>();
            try {
                SearchResult result = client.execute(search);
                reviews = result.getSourceAsObjectList(Review.class);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return (ArrayList<Review>) reviews;
        }
    }

    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        Log.i("going to", "verify settings");
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
        Log.i("finished", "verify");
    }

    protected static ArrayList<Request> filterRequestsInProgress(ArrayList<Request> requests) {
        ArrayList<Request> result = new ArrayList<Request>();
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getWaitingForRider()) {
                result.add(requests.get(i));
            }
        }
        return result;
    }

}