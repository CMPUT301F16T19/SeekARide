package com.c301t19.cs.ualberta.seekaride.core;

import io.searchbox.annotations.JestId;

/**
 * Review class contains the info for rating and description
 */
public class Review {

    private String description;
    private float rating;
    private String userID;
    @JestId
    // Review ID
    protected String id;

    /**
     * constructor
     * @param description
     * @param rating
     * @param userID
     */
    public Review(String description, float rating, String userID) {
        this.description = description;
        this.rating = rating;
        this.userID = userID;
    }

    /**
     * get the description
     * @return string of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the rating
     * @return float of rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * set rating
     * @param rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * get the user ID
     * @return userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * set userID
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * get the id
     * @return id
     */
    public String getId() {
        return id;
    }


}
