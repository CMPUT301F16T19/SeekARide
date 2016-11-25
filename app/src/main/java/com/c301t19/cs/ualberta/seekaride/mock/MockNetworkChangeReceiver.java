package com.c301t19.cs.ualberta.seekaride.mock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.NetworkChangeReceiver;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockNetworkChangeReceiver {

    public static void onReceive() {
        NetworkManager.Connectivity status = MockNetworkManager.getInstance().getConnectivityStatus();
        switch (status) {
            case WIFI:
                if (MockRider.getInstance() == null) {
                    return;
                }
                MockRider.getInstance().executeRiderCommands();
                if (MockDriver.getInstance() == null) {
                    return;
                }
                MockDriver.getInstance().executeDriverCommands();
                break;
            case MOBILE:
                if (MockRider.getInstance() == null) {
                    return;
                }
                MockRider.getInstance().executeRiderCommands();
                if (MockDriver.getInstance() == null) {
                    return;
                }
                MockDriver.getInstance().executeDriverCommands();
                break;
            case NONE:
                break;
            default:
                break;
        }
    }
}
