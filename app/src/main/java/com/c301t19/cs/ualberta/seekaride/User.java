package com.c301t19.cs.ualberta.seekaride;

/**
 * Created by mc on 16/10/13.
 */
public class User {
    public RiderRequest getRequest() {
        return new RiderRequest();
    }

    public void setRequest(RiderRequest request) {
    }

    public void setProfile(Profile profile) {
    }

    public Profile getProfile() {
        return new Profile();
    }
}
