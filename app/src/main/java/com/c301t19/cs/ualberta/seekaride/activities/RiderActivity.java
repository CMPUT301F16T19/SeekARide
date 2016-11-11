package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.*;

import java.util.concurrent.ExecutionException;

public class RiderActivity extends Activity {

    Rider rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        // instantiate a Rider with a Profile obtained from Elasticsearch
        // search Elasticsearch using the username when logging in
        // if user exists, retrieve profile
        // else make new profile
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        try {
            Profile profile = getUserTask.get();
            if (profile == null) {
                Profile newProfile = new Profile(username, "PHONE", "EMAIL");
                ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(newProfile);
                
            }
            else {
                rider = new Rider(profile);
            }
        }
        catch (Exception e) {

        }
    }
}
