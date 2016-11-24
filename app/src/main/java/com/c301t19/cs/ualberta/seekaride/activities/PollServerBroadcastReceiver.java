package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

// http://simpleandroidtutorials.blogspot.ca/2012/06/periodically-update-data-from-server-in.html 2016-11-12, 3:02 PM, author Nirali
public class PollServerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Rider.getInstance() == null || Driver.getInstance() == null) {
            return;
        }
        Intent dailyUpdater = new Intent(context, PollingService.class);
        context.startService(dailyUpdater);
        Log.i("AlarmReceiver", "Called context.startService from AlarmReceiver.onReceive");
    }
}

