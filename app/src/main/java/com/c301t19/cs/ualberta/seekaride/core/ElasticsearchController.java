package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.searchbox.core.Index;

public class ElasticsearchController {

    // used to indicate search/edit targets
    public enum UserField { NAME, EMAIL, PHONE }
    public enum RequestField { DESCRIPTION, START, END, DATE, RIDER, DRIVERS }

    private static JestDroidClient client;

    // add a User to elasticsearch. returns true on success
    public static class AddUserTask extends AsyncTask<Void, Void, Boolean> {

        private Profile user;

        public AddUserTask(Profile u) {
            super();
            user = u;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifySettings();
            String username = user.getUsername();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            Map<String, String> source = new LinkedHashMap<String,String>();
            source.put("username", username);
            source.put("email", email);
            source.put("phone", phone);
            Index index = new Index.Builder(source).index("T19SeekARide").type("User").build();
            try {
                client.execute(index);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return Boolean.TRUE;
        }
    }

    // gets a User from elasticsearch. pass in the field you want to search by and the search string
    public static class GetUserTask extends AsyncTask<Void, Void, User> {

        private UserField userField;
        private String keyword;

        public GetUserTask(UserField uf, String k) {
            super();
            userField = uf;
            keyword = k;
        }

        @Override
        protected User doInBackground(Void... params) {
            return null;
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

    // add a Request to elasticsearch. return true on success
    public static class AddRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Request request;

        public AddRequestTask(Request r) {
            super();
            request = r;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }

    // gets a Request from elasticsearch. meant to be used when you have a specific request
    // you are looking for, rather than for searching over many requests. mostly likely will
    // be used with a request ID
    public static class GetRequestTask extends AsyncTask<Void, Void, Request> {

        private RequestField requestField;
        private String keyword;

        public GetRequestTask(RequestField rf, String k) {
            super();
            requestField = rf;
            keyword = k;
        }

        @Override
        protected Request doInBackground(Void... params) {
            return null;
        }
    }

    // searches for Requests from elasticsearch. pass in location and radius; get array of Results
    // does not distinguish between start and end location; a class that uses this method should handle that on their own
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
            return null;
        }
    }

    // searches for Requests from elasticsearch. pass in array of strings; get array of Results
    public static class SearchRequestsByKeywordTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private ArrayList<String> keywords;

        public SearchRequestsByKeywordTask(ArrayList<String> k) {
            super();
            keywords = k;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            return null;
        }
    }

    // simply overwrites a request with an updated request
    // if security is desired, in Request class, protect methods like deleteRequest and setRider with a check like
    // "if requestRider.name != currentUser.name: return"
    // this way drivers will only be allowed to add/remove driver. make sure the driver they are adding or removing = currentDriver.name
    public static class EditRequestTask extends AsyncTask<UserField, Void, User> {

        @Override
        protected User doInBackground(UserField... params) {
            return null;
        }
    }

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