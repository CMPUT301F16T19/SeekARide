package com.c301t19.cs.ualberta.seekaride.core;

import android.content.Context;

/**
 * Base Controller class for a User. Inherited by Rider and Driver.
 */
public class User {

    private Request requestInProgress;

    private boolean receivedNotification;

    /**
     * The Profile.
     */
    protected Profile profile;

    /**
     * Instantiates a new User.
     *
     * @param profile the profile
     */
    public User(Profile profile) {

        this.profile = profile;
        requestInProgress = null;
        receivedNotification = false;
    }

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Contacts another user by phone.
     * Issues: unimplemented
     *
     * @param phone Phone number of the user to contact.
     */
    @Deprecated
    public void contactByPhone(String phone) {

    }

    /**
     * Contacts another user by email.
     * Issues: unimplemented
     *
     * @param email Email address of the user to contact.
     */
    @Deprecated
    public void contactByEmail(String email) {

    }

    /**
     * Given a start and end Location, suggests an offering price.
     * Issues: unimplemented
     *
     * @param start Start Location.
     * @param end   End Location.
     * @return Suggested pricing.
     */
    @Deprecated
    public float getRecommendedPrice(Location start, Location end) {
        return 0;
    }

    /**
     * Sets profile.
     *
     * @param newProfile the new profile
     */
    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }

    /**
     * Search phone string.
     *
     * @param username the username
     * @return the string
     */
    @Deprecated
    public String searchPhone(String username) {
        String phoneNumber = "idk";// should change to elastic search here
        return phoneNumber;
    }

    public void leaveReview(Review review) {
        ElasticsearchController.AddReviewTask addReviewTask = new ElasticsearchController.AddReviewTask(review);
        addReviewTask.execute();
    }

    public Request getRequestInProgress() {
        return requestInProgress;
    }

    public void setRequestInProgress(Request requestInProgres) {
        requestInProgress = requestInProgres;
    }

    public void clearRequestInProgress() {
        requestInProgress = null;
    }

    public boolean hasReceivedNotification() {
        return receivedNotification;
    }

    public void setReceivedNotification(boolean receivedNotification) {
        this.receivedNotification = receivedNotification;
    }

}
