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

    /**
     * Logs the user in with their desired username. A new account is created if a user doesn't already exist
     * with the given name. The name must be at least five characters long. Rider and Driver controller classes
     * are instantiated with a Profile generated from the username.
     *
     * @param username The user's username.
     * @return true if the login was successful.
     */
    public boolean login(Profile profile) {
        if (profile.getUsername().length() <  5)
        {
            return false;
        }
        MockRider.instantiate(profile);
        MockDriver.instantiate(profile);
        return true;
    }

    @Override
    public boolean createNewAccount(Profile profile) {
        return true;
    }

    @Override
    public void editAccount(Profile oldProfile, Profile newProfile) {
        MockRider.instantiate(newProfile);
        MockDriver.instantiate(newProfile);
    }
}
