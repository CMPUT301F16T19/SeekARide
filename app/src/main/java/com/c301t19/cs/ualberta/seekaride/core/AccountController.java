package com.c301t19.cs.ualberta.seekaride.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.c301t19.cs.ualberta.seekaride.activities.PollingService;
import com.c301t19.cs.ualberta.seekaride.activities.PollServerBroadcastReceiver;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Controller that handles login and account creation. Not reliant upon Rider/Driver being instantiated.
 */
public class AccountController {

    /**
     * Instantiates a new Login controller.
     */
    public AccountController() {

    }

    /**
     * Logs the user in with their desired username. The name must be at least five characters long.
     * Rider and Driver controller classes are instantiated with a Profile generated from the username.
     * Also begins to poll the server to update Rider's openRequests and Driver's acceptedRequests.
     * @param username The user's username.
     * @return true if the login was successful.
     */
    public boolean login(String username, Context context) {
        if (username.length() <  5)
        {
            return false;
        }
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        Profile profile;
        try {
            profile = getUserTask.get();
            if (profile == null) {
                return false;
            }
            else {
                Rider.instantiate(profile);
                Driver.instantiate(profile);
                // restart service when logging in
                pollServer(context);
            }
        }
        catch (Exception e) {

        }
        return true;
    }

    /**
     * Creates a new account if the username is unique.
     * @param profile The profile of the new account.
     * @return true if successful. false if the username is not unique
     */
    public boolean createNewAccount(Profile profile) {
        if (checkUserExists(profile.getUsername())) {
            return false;
        }
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(profile);
        addUserTask.execute();
        return true;
    }

    /**
     * Edits an account.
     * @param oldProfile Profile before editing.
     * @param newProfile Profile after editing.
     */
    public void editAccount(Profile oldProfile, Profile newProfile) {
        ElasticsearchController.DeleteUserTask deleteUserTask = new ElasticsearchController.DeleteUserTask(oldProfile);
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(newProfile, newProfile.getId());
        deleteUserTask.execute();
        addUserTask.execute();
        Rider.instantiate(newProfile);
        Driver.instantiate(newProfile);
    }

    protected boolean checkUserExists(String username) {
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        Profile profile;
        try {
            profile = getUserTask.get();
            if (profile == null) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception e) {

        }
        return true;
    }

    // http://simpleandroidtutorials.blogspot.ca/2012/06/periodically-update-data-from-server-in.html 2016-11-12, 3:02 PM, author Nirali
    private void pollServer(Context context) {

        context.stopService(new Intent(context, PollingService.class));

        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getDefault());
        updateTime.set(Calendar.HOUR_OF_DAY, 12);
        updateTime.set(Calendar.MINUTE, 30);
        Intent intent = new Intent(context, PollServerBroadcastReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 1000*10, pendingIntent);
    }
}
