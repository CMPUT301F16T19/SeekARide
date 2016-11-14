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

import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Allows asynchronous interaction with the Elasticsearch database.
 *
 * Issues: this class does a lot. It may need to be broken down later.
 */
public class ElasticsearchController {

    /**
     * Indicates a field in a user's Profile for search tasks.
     */
    public enum UserField { NAME, EMAIL, PHONE }

    /**
     * Indicates a field in a Request for search tasks.
     */
    public enum RequestField { DESCRIPTION, START, END, DATE, RIDERID, DRIVERS }

    private static JestDroidClient client;

    /**
     * Adds a Profile to Elasticsearch.
     */
    public static class AddUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile user;

        public AddUserTask(Profile u) {
            super();
            user = u;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            /*
            String username = user.getUsername();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            */

            /*
            Map<String, String> source = new LinkedHashMap<String,String>();
            source.put("username", username);
            source.put("email", email);
            source.put("phone", phone);
            Index index = new Index.Builder(source).index("t19seekaride").type("user").build();
            */

            Index index = new Index.Builder(user).index("t19seekaride").type("user").build();
            /*
            String source = "{\n" +
                                "\"username\" : " + username + ",\n" +
                                "\"email\" : " + email + ",\n" +
                                "\"phone\" : " + phone + "\n" +
                           "}";
            Index index = new Index.Builder(source).index("t19seekaride").type("user").build();*/
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
     *
     * Issues: Currently only allows you to search by username.
     */
    public static class GetUserTask extends AsyncTask<Void, Void, Profile> {

        private UserField userField;
        private String keyword;

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

    // edit a User. pass in the field you want to edit and the new value. return true on success
    public static class EditUserTask extends AsyncTask<Void, Void, Boolean> {

        private UserField userField;
        private String newValue;

        public EditUserTask(UserField uf, String nv) {
            super();
            userField = uf;
            newValue = nv;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }

    /**
     * Add a Request to Elasticsearch.
     */
    public static class AddRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;

        public AddRequestTask(Request r) {
            super();
            request = r;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index = new Index.Builder(request).index("t19seekaride").type("request").build();
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
     *
     * Issues: Currently only allows you to search by the Rider's id.
     */
    public static class GetRequestsTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private RequestField requestField;
        private String keyword;

        public GetRequestsTask(RequestField rf, String k) {
            super();
            requestField = rf;
            keyword = k;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            verifySettings();
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"riderId\" : \"" + keyword + "\"\n" +
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

    public static class SearchRequestsByLocationTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private Location location;
        private int radius;

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
     *
     * Issues: Currently only allows you to search by keyword, which only targets a request's description.
     */
    public static class SearchRequestsByKeywordTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private String keywords;

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

    // simply overwrites a request with an updated request
    // if security is desired, in Request class, protect methods like deleteRequest and setRider with a check like
    // "if requestRider.name != currentUser.name: return"
    // this way drivers will only be allowed to add/remove driver. make sure the driver they are adding or removing = currentDriver.name
    /*public static class EditRequestTask extends AsyncTask<Void, Void, Boolean> {

        private RequestField requestField;
        private Object editValue;

        public EditRequestTask(RequestField rf, Object ev) {
            requestField = rf;
            editValue = ev;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            Index index = new Index.Builder(request).index("t19seekaride").type("request").build();
            try {
                client.execute(index);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }*/

    // IMPLEMENTATION UNCLEAR
    public static class DeleteRequestTask extends AsyncTask<UserField, Void, User> {

        @Override
        protected User doInBackground(UserField... params) {
            return null;
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

    /* example usage:
    ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                                                      UserField.NAME, "username");
		getUserTask.execute();
		try {
			user = getUserTask.get();
		}
		catch (Exception e) {
		}
    */
}