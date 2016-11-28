package com.c301t19.cs.ualberta.seekaride.core;

/**
 * Base Controller class for a User. Inherited by Rider and Driver.
 */
public class User {

    private Request requestInProgress;

    private boolean receivedNotification;

    /**
     * The User's Profile.
     */
    protected Profile profile;

    /**
     * Instantiates a new User.
     *
     * @param profile a Profile to instantiate the User with
     */
    public User(Profile profile) {

        this.profile = profile;
        requestInProgress = null;
        receivedNotification = false;
    }

    /**
     * Gets profile.
     *
     * @return the User's Profile
     */
    public Profile getProfile() {
        return profile;
    }


    /**
     * Sets profile.
     *
     * @param newProfile the new Profile to give the user
     */
    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }


    /**
     * Add a review to Elasticsearch
     * @see ElasticsearchController
     * @param review a Review to add to Elasticsearch
     */
    public void leaveReview(Review review) {
        ElasticsearchController.AddReviewTask addReviewTask = new ElasticsearchController.AddReviewTask(review);
        addReviewTask.execute();
    }

    /**
     * Get the current request in progress, if it exists.
     * @return the request in progress. Null if no request is in progress
     */
    public Request getRequestInProgress() {
        return requestInProgress;
    }

    /**
     * Begin a request when Rider and Driver have both accepted.
     * @param requestInProgress The request to start.
     */
    public void setRequestInProgress(Request requestInProgress) {
        this.requestInProgress = requestInProgress;
    }

    /**
     * Sets the requestInProgress to null. To be called when a request is completed.
     */
    public void clearRequestInProgress() {
        requestInProgress = null;
    }

    /**
     * Returns true if the user has already been notified recently of an accepted request.
     * @return true if the user has already been notified recently of an accepted request.
     */
    public boolean hasReceivedNotification() {
        return receivedNotification;
    }

    /**
     * Set to true once the user receives a notification of an accepted request.
     * Set to false once the request is complete and the user can start a new request.
     * @param receivedNotification
     */
    public void setReceivedNotification(boolean receivedNotification) {
        this.receivedNotification = receivedNotification;
    }

}
