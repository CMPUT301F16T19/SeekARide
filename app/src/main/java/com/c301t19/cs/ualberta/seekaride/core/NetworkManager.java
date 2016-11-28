package com.c301t19.cs.ualberta.seekaride.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * manage the network
 */
// http://stackoverflow.com/questions/18632823/how-to-monitor-network-status-in-android 2016-11-16, author Vipul Purohit
public class NetworkManager {

    public enum Connectivity { WIFI, MOBILE, NONE };

    private Context context;
    private static NetworkManager instance = null;

    /**
     * constructor
     * @param context
     */
    private NetworkManager(Context context) {
        this.context = context;
    }

    /**
     * constructor
     */
    protected NetworkManager() {}

    /**
     * get instance
     * @return
     */
    public static NetworkManager getInstance() {
        return instance;
    }

    /**
     * instantiate
     * @param context
     */
    public static void instantiate(Context context) {
        instance = new NetworkManager(context);
    }

    /**
     * get Connectivity Status
     * @return ConnectivityStatus
     */
    public Connectivity getConnectivityStatus() {
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

    /**
     * get Connectivity Status
     * @return Connectivity Status with string
     */
    public String getConnectivityStatusString() {
        Connectivity conn = getConnectivityStatus();
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
