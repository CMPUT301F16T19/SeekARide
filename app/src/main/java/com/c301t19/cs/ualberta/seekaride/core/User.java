package com.c301t19.cs.ualberta.seekaride.core;

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
     * Sets profile.
     *
     * @param newProfile the new profile
     */
    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }


    /**
     * add the review to elastic search
     * @see ElasticsearchController
     * @param review
     */
    public void leaveReview(Review review) {
        ElasticsearchController.AddReviewTask addReviewTask = new ElasticsearchController.AddReviewTask(review);
        addReviewTask.execute();
    }

    /**
     * get the state of requestInProgress
     * @return a boolean
     */
    public Request getRequestInProgress() {
        return requestInProgress;
    }

    /**
     * change the state of requestInProgress
     * @param requestInProgress
     */
    public void setRequestInProgress(Request requestInProgress) {
        this.requestInProgress = requestInProgress;
    }

    /**
     * set the requestInProgress to null
     */
    public void clearRequestInProgress() {
        requestInProgress = null;
    }

    /**
     * get the state of notification
     * @return a boolean
     */
    public boolean hasReceivedNotification() {
        return receivedNotification;
    }

    /**
     * change the state of notification
     * @param receivedNotification
     */
    public void setReceivedNotification(boolean receivedNotification) {
        this.receivedNotification = receivedNotification;
    }

}
