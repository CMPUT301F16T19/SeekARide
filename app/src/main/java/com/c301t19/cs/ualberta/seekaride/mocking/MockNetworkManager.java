package com.c301t19.cs.ualberta.seekaride.mocking;

import android.content.Context;

import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;

/**
 * Created by mc on 16/11/24.
 */
public class MockNetworkManager extends NetworkManager {

    private static MockNetworkManager instance = null;

    private MockNetworkManager(Context c) {
        super(c);
    }

    public static MockNetworkManager getInstance() {
        return instance;
    }

    public static void instantiate(Context c) {
        instance = new MockNetworkManager(c);
    }
}
