package com.c301t19.cs.ualberta.seekaride.core;

/**
 * Base Controller class for a User. Inherited by Rider and Driver.
 */
public class User {

    protected Profile profile;

    public User(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    /**
     * Contacts another user by phone.
     * Issues: unimplemented
     * @param phone Phone number of the user to contact.
     */
    public void contactByPhone(String phone) {

    }

    /**
     * Contacts another user by email.
     * Issues: unimplemented
     * @param email Email address of the user to contact.
     */
    public void contactByEmail(String email) {

    }

    /**
     * Given a start and end Location, suggests an offering price.
     * Issues: unimplemented
     * @param start Start Location.
     * @param end End Location.
     * @return Suggested pricing.
     */
    public float getRecommendedPrice(Location start, Location end) {
        return 0;
    }

    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }

    public String searchPhone(String username) {
        String phoneNumber = "idk";// should change to elastic search here
        return phoneNumber;
    }
}
