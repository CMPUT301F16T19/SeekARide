package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        PHONE }

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
        DRIVERS, ID }

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
                index = new Index.Builder(user).index("t19seekaride").type("user").build();
            }
            else
            {
                index = new Index.Builder(user).index("t19seekaride").type("user").id(id).build();
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
     * Issues: Currently only allows you to search by username.
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
            String query = "{\n" +
                           "    \"query\": {\n" +
                           "        \"filtered\" : {\n" +
                           "            \"filter\" : {\n" +
                           "                \"term\" : { \"username\" : \"" + keyword + "\" }\n" +
                           "            }\n" +
                           "        }\n" +
                           "    }\n" +
                           "}";
            Search search = new Search.Builder(query)
                            .addIndex("t19seekaride")
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
                return profiles.get(0);
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
            Delete delete  = new Delete.Builder(user.getId()).index("t19seekaride").type("user").build();
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
                index = new Index.Builder(request).index("t19seekaride").type("request").build();
            }
            else {
                index = new Index.Builder(request).index("t19seekaride").type("request").id(id).build();
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
     * Retrieves an ArrayList of Requests from Elasticsearch.
     * <p/>
     * Issues: Currently only allows you to search by the Rider's id.
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
                    query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"id\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    break;
                case RIDERID:
                    query = "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : {\n" +
                            "            \"riderId\" : \"" + keyword + "\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                    break;
                default:
                    Log.i("dont", "plz no");
                    return null;
            }
            Search search = new Search.Builder(query)
                    .addIndex("t19seekaride")
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
        private int radius;

        /**
         * Instantiates a new Search requests by location task.
         *
         * @param l the l
         * @param r the r
         */
        public SearchRequestsByLocationTask(Location l, int r) {
            super();
            location = l;
            radius = r;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();

            return null;
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
                    .addIndex("t19seekaride")
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
                delete  = new Delete.Builder(request.getId()).index("t19seekaride").type("request").build();
            }
            else {
                delete  = new Delete.Builder(id).index("t19seekaride").type("request").build();
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
}