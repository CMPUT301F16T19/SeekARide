package com.c301t19.cs.ualberta.seekaride.core;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class ElasticsearchController {

    // used to indicate search/edit targets
    public enum UserField { NAME, EMAIL, PHONE }
    public enum RequestField { START, END, DATE, RIDER, DRIVERS }

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

    // gets a User from elasticsearch. pass in the field you want to search by
    public static class GetUserTask extends AsyncTask<Void, Void, User> {

        private UserField userField;

        public GetUserTask(UserField uf) {
            super();
            userField = uf;
        }

        @Override
        protected User doInBackground(Void... params) {
            return null;
        }
    }

    // edit a User. pass in the field you want to edit and the new value. return true on success
    public static class EditUserTask extends AsyncTask<Void, Void, Boolean> {

        private UserField userField;

        public EditUserTask(UserField uf) {
            super();
            userField = uf;
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

    // gets a User from elasticsearch. pass in the field you want to search by
    public static class GetUserTask extends AsyncTask<UserField, Void, User> {

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

    // gets a User from elasticsearch. pass in the field you want to search by
    public static class GetUserTask extends AsyncTask<UserField, Void, User> {

        @Override
        protected User doInBackground(UserField... params) {
            return null;
        }
    }

    // gets a User from elasticsearch. pass in the field you want to search by
    public static class GetUserTask extends AsyncTask<UserField, Void, User> {

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