package com.c301t19.cs.ualberta.seekaride.core;

import io.searchbox.annotations.JestId;

/**
 * Represents a user's review of another user.
 */
public class Review {

    private String description;
    private float rating;
    private String userID;
    @JestId
    // Review ID
    protected String id;

    /**
     * Constructor
     * @param description The main review text.
     * @param rating A numerical rating from 1-5.
     * @param userID The ID of the user to be reviewed.
     */
    public Review(String description, float rating, String userID) {
        this.description = description;
        this.rating = rating;
        this.userID = userID;
    }

    /**
     * Get the description
     * @return User-defined review text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description
     * @param description User-defined review text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the rating
     * @return User-defined rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * Set rating
     * @param rating User-defined rating from 1-5
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Get the user ID
     * @return Id of the user being reviewed
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set userID
     * @param userIDId of the user being reviewed
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Get the review Id
     * @return id Unique identifier for the review
     */
    public String getId() {
        return id;
    }


}
