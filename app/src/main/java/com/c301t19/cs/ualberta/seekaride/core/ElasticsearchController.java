package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class ElasticsearchController {

    // used to indicate search/edit targets
    public enum UserField { NAME, EMAIL, PHONE }
    public enum RequestField { DESCRIPTION, START, END, DATE, RIDER, DRIVERS }

    private static JestDroidClient client;

    // add a User to elasticsearch. returns true on success
    public static class AddUserTask extends AsyncTask<Void, Void, Boolean> {

        private User user;

        public AddUserTask(User u) {
            super();
            user = u;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
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

    // searches for Requests from elasticsearch. pass in an object of the following structure:
    // ArrayList<Map<RequestField, ArrayList<String>>>
    // maps fields to a list of search terms, and more than one field can be searched (using OR logic)
    // returns list of requests
    public static class SearchRequestsTask extends AsyncTask<Void, Void, ArrayList<Request>> {

        private ArrayList<Map<RequestField, ArrayList<String>>> searchParams;

        public SearchRequestsTask(ArrayList<Map<RequestField, ArrayList<String>>> sp) {
            super();
            searchParams = sp;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            return null;
        }
    }

    // searches for Requests from elasticsearch. similar to the above, but just takes one field and one search term
    public static class SearchRequestsTaskSimple extends AsyncTask<Void, Void, ArrayList<Request>> {

        private RequestField requestField;
        private String keyword;

        public SearchRequestsTaskSimple(RequestField rf, String k) {
            super();
            requestField = rf;
            keyword = k;
        }

        @Override
        protected ArrayList<Request> doInBackground(Void... params) {
            return null;
        }
    }

    // IMPLEMENTATION UNCLEAR
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