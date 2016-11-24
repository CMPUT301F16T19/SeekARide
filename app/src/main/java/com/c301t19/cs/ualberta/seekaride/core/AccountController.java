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
 * Controller that handles login and account creation.
 * <p/>
 * Issues: may want to implement a more rigorous account creation/login process in the future.
 */
public class AccountController {

    /**
     * Instantiates a new Login controller.
     */
    public AccountController() {

    }

    /**
     * Logs the user in with their desired username. A new account is created if a user doesn't already exist
     * with the given name. The name must be at least five characters long. Rider and Driver controller classes
     * are instantiated with a Profile generated from the username.
     *
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

    public boolean createNewAccount(Profile profile) {
        if (checkUserExists(profile.getUsername())) {
            return false;
        }
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(profile);
        addUserTask.execute();
        return true;
    }

    public void editAccount(Profile oldProfile, Profile newProfile) {
        ElasticsearchController.DeleteUserTask deleteUserTask = new ElasticsearchController.DeleteUserTask(oldProfile);
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(newProfile, newProfile.getId());
        deleteUserTask.execute();
        addUserTask.execute();
        Rider.instantiate(newProfile);
        Driver.instantiate(newProfile);
    }

    private boolean checkUserExists(String username) {
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
