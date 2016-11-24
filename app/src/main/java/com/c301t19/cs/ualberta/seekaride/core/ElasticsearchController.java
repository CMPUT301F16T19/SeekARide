package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.osmdroid.util.GeoPoint;

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
 * <p/>
 * Issues: this class does a lot. It may need to be broken down later.
 */
public class ElasticsearchController {

    public static final String INDEX = "t19seekaride2";

    public enum ReviewField {
        USERID, ID
    }
    /**
     * Indicates a field in a user's Profile for search tasks.
     */
    public enum UserField {
        /**
         * Name user field.
         */
        NAME, /**
         * Email user field.
         */
        EMAIL, /**
         * Phone user field.
         */
        PHONE, ID }

    /**
     * Indicates a field in a Request for search tasks.
     */
    public enum RequestField {
        /**
         * Description request field.
         */
        DESCRIPTION, /**
         * Start request field.
         */
        START, /**
         * End request field.
         */
        END, /**
         * Date request field.
         */
        DATE, /**
         * Riderid request field.
         */
        RIDERID, /**
         * Drivers request field.
         */
        DRIVERID, ID }

    private static JestDroidClient client;

    /**
     * Adds a Profile to Elasticsearch.
     */
    public static class AddUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile user;
        private String id;

        /**
         * Instantiates a new Add user task.
         *
         * @param u the u
         */
        public AddUserTask(Profile u) {
            super();
            user = u;
            id = null;
        }

        public AddUserTask(Profile u, String i) {
            super();
            user = u;
            id = i;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index;
            if (id == null)
            {
                index = new Index.Builder(user).index(INDEX).type("user").build();
            }
            else
            {
                index = new Index.Builder(user).index(INDEX).type("user").id(id).build();
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
     * Retrieves a Profile from Elasticsearch.
     * <p/>
     */
    public static class GetUserTask extends AsyncTask<Void, Void, Profile> {

        private UserField userField;
        private String keyword;

        /**
         * Instantiates a new Get user task.
         *
         * @param uf the uf
         * @param k  the k
         */
        public GetUserTask(UserField uf, String k) {
            super();
            userField = uf;
            keyword = k;
        }

        @Override
        protected Profile doInBackground(Void... params) {
            verifySettings();
            switch (userField) {
                case NAME:
                    /*String query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"filtered\" : {\n" +
                            "            \"filter\" : {\n" +
                            "                \"term\" : { \"username\" : \"" + keyword + "\" }\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";*/
                    String query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"username\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    Search search = new Search.Builder(query)
                            .addIndex(INDEX)
                            .addType("user")
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
                    Get get = new Get.Builder(INDEX, keyword).type("user").build();
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

    public static class DeleteUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile user;

        public DeleteUserTask(Profile u) {
            super();
            user = u;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Delete delete  = new Delete.Builder(user.getId()).index(INDEX).type("user").build();
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
     * Add a Request to Elasticsearch.
     */
    public static class AddRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;
        private String id;

        /**
         * Instantiates a new Add request task.
         *
         * @param r the r
         */
        public AddRequestTask(Request r) {
            super();
            request = r;
            id = null;
        }

        public AddRequestTask(Request r, String i) {
            super();
            request = r;
            id = i;
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
     * Retrieves an ArrayList of Requests from Elasticsearch. Currently allows you to search by RiderID, DriverID, and RequestID
     */
    public static class GetRequestsTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private RequestField requestField;
        private String keyword;

        /**
         * Instantiates a new Get requests task.
         *
         * @param rf the rf
         * @param k  the k
         */
        public GetRequestsTask(RequestField rf, String k) {
            super();
            requestField = rf;
            keyword = k;
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
     * The type Search requests by location task.
     */
    public static class SearchRequestsByLocationTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private Location location;
        private double radius;

        /**
         * Instantiates a new Search requests by location task.
         *
         * @param l the l
         * @param r the r
         */
        public SearchRequestsByLocationTask(Location l, double r) {
            super();
            location = l;
            radius = r;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();
            String radiusToString;
            radiusToString = String.valueOf(radius);
            String locationArray;
            locationArray = "[" + String.valueOf(location.getGeoLocation().getLongitude())
                    + "," + String.valueOf(location.getGeoLocation().getLatitude()) + "]";
            String query = "{\n" +
                    "    \"filter\": {\n" +
                    "        \"geo_distance\" : {\n" +
                    "            \"distance\" : \"" + radiusToString + "m\",\n" +
                    "            \"location\" :" + locationArray + "\n" +
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
     * Retrieves an ArrayList of Requests from Elasticsearch.
     * <p/>
     * Issues: Currently only allows you to search by keyword, which only targets a request's description.
     */
    public static class SearchRequestsByKeywordTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private String keywords;

        /**
         * Instantiates a new Search requests by keyword task.
         *
         * @param k the k
         */
        public SearchRequestsByKeywordTask(String k) {
            super();
            keywords = k;
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
     * The type Delete request task.
     */
    public static class DeleteRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;
        private String id;

        public DeleteRequestTask(Request r) {
            super();
            request = r;
            id = null;
        }

        public DeleteRequestTask(String i) {
            super();
            request = null;
            id = i;
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

    public static class AddReviewTask extends AsyncTask<Void, Void, Boolean> {

        private Review review;

        public AddReviewTask(Review r) {
            super();
            review = r;
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

    public static class GetReviewsTask extends AsyncTask<Void, Void, ArrayList<Review>> {

        private String userID;

        public GetReviewsTask(String u) {
            super();
            userID = u;
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
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    private static ArrayList<Request> filterRequestsInProgress(ArrayList<Request> requests) {
        ArrayList<Request> result = new ArrayList<Request>();
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getWaitingForRider()) {
                result.add(requests.get(i));
            }
        }
        return result;
    }

}