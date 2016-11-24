package com.c301t19.cs.ualberta.seekaride.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// http://stackoverflow.com/questions/18632823/how-to-monitor-network-status-in-android 2016-11-16, author Vipul Purohit
public class NetworkManager {

    public enum Connectivity { WIFI, MOBILE, NONE };

    public static Connectivity getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return Connectivity.WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return Connectivity.MOBILE;
        }
        return Connectivity.NONE;
    }

    public static String getConnectivityStatusString(Context context) {
        Connectivity conn = NetworkManager.getConnectivityStatus(context);
        String status = null;
        if (conn == Connectivity.WIFI) {
            status = "Wifi enabled";
        } else if (conn == Connectivity.MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == Connectivity.NONE) {
            status = "Not connected to Internet";
        }
        return status;
    }
}
