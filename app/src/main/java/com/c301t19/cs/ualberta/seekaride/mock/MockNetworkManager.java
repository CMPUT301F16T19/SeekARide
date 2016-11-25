package com.c301t19.cs.ualberta.seekaride.mock;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockNetworkManager extends NetworkManager {

    private static Connectivity conn;

    private static MockNetworkManager instance = null;
    private MockNetworkManager(){

    }
    public static MockNetworkManager getInstance() {
        return instance;
    }
    public static void instantiate(Connectivity c) {
        instance = new MockNetworkManager();
        conn = c;
    }

    public void setConnectivityStatus(Connectivity c) {
        conn = c;
    }

    @Override
    public Connectivity getConnectivityStatus() {
        return conn;
    }

    @Override
    public String getConnectivityStatusString() {
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
