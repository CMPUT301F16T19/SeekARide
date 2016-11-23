package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Master Chief on 2016-11-23.
 */
public class PollServerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent dailyUpdater = new Intent(context, PollingService.class);
        context.startService(dailyUpdater);
        Log.i("AlarmReceiver", "Called context.startService from AlarmReceiver.onReceive");
    }
}

