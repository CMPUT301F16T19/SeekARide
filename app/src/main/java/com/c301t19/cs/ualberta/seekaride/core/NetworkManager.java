package com.c301t19.cs.ualberta.seekaride.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Singleton class that describes the current state of the internet connection.
 */
// http://stackoverflow.com/questions/18632823/how-to-monitor-network-status-in-android 2016-11-16, author Vipul Purohit
public class NetworkManager {

    /**
     * enumeration describing possible connectivity values
     */
    public enum Connectivity { WIFI, MOBILE, NONE };

    private Context context;
    private static NetworkManager instance = null;

    private NetworkManager(Context context) {
        this.context = context;
    }
    protected NetworkManager() {}

    /**
     * Get the instance of NetworkManager
     * @return instance
     */
    public static NetworkManager getInstance() {
        return instance;
    }

    /**
     * Instantiates the NetworkManager. Must be called before any other methods are called. Should be called first thing
     * when the app launches to immediately begin monitoring the network connection
     * @param context Android's application context.
     */
    public static void instantiate(Context context) {
        instance = new NetworkManager(context);
    }

    /**
     * Get the current connectivity
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
     * @return String description of connectivity
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
