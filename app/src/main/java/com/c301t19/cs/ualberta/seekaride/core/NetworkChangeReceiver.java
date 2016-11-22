package com.c301t19.cs.ualberta.seekaride.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// http://stackoverflow.com/questions/18632823/how-to-monitor-network-status-in-android 2016-11-16, author Vipul Purohit
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        NetworkManager.Connectivity status = NetworkManager.getConnectivityStatus(context);

        // do something with the change in network connection
        switch (status) {
            case WIFI:
                if (Rider.getInstance() == null) {
                    return;
                }
                Rider.getInstance().executeRiderCommands();
                if (Driver.getInstance() == null) {
                    return;
                }
                Driver.getInstance().executeDriverCommands();
                break;
            case MOBILE:
                if (Rider.getInstance() == null) {
                    return;
                }
                Rider.getInstance().executeRiderCommands();
                if (Driver.getInstance() == null) {
                    return;
                }
                Driver.getInstance().executeDriverCommands();
                break;
            case NONE:
                break;
            default:
                break;
        }

        String status2 = NetworkManager.getConnectivityStatusString(context);

        Toast.makeText(context, status2, Toast.LENGTH_LONG).show();
    }
}