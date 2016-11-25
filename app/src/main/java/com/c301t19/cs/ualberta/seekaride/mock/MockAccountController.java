package com.c301t19.cs.ualberta.seekaride.mock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.c301t19.cs.ualberta.seekaride.activities.PollServerBroadcastReceiver;
import com.c301t19.cs.ualberta.seekaride.activities.PollingService;
import com.c301t19.cs.ualberta.seekaride.core.AccountController;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockAccountController extends AccountController {

    /**
     * Instantiates a new Login controller.
     */
    public MockAccountController() {

    }

    @Override
    protected boolean checkUserExists(String username) {
        Profile profile = MockElasticsearchController.GetUserTask(ElasticsearchController.UserField.NAME, username);
            if (profile == null) {
                return false;
            }
            else {
                return true;
            }
    }

    public boolean login(String username) {
        if (username.length() <  5)
        {
            return false;
        }
        Profile profile = MockElasticsearchController.GetUserTask(ElasticsearchController.UserField.NAME, username);
            if (profile == null) {
                return false;
            }
            else {
                MockRider.instantiate(profile);
                MockDriver.instantiate(profile);
                return true;
            }
        }

    @Override
    public boolean createNewAccount(Profile profile) {
        if (checkUserExists(profile.getUsername())) {
            return false;
        }
        MockElasticsearchController.AddUserTask(profile);
        return true;
    }

    @Override
    public void editAccount(Profile oldProfile, Profile newProfile) {
        MockElasticsearchController.DeleteUserTask(oldProfile);
        MockElasticsearchController.AddUserTask(newProfile);
        MockRider.instantiate(newProfile);
        MockDriver.instantiate(newProfile);
    }
}
