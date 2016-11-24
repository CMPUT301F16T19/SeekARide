package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

/**
 * Created by Master Chief on 2016-11-23.
 */
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

