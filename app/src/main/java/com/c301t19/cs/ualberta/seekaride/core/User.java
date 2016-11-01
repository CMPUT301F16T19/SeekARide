package com.c301t19.cs.ualberta.seekaride.core;

/**
 * Created by mc on 16/10/13.
 */
public class User {

    protected Profile profile;

    public User(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void contactByPhone(String phone) {

    }

    public void contactByEmail(String email) {

    }

    public float getRecommendedPrice(Location start, Location end) {
        return 0;
    }
}
