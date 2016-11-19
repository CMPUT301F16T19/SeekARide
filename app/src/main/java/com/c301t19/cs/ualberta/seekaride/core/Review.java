package com.c301t19.cs.ualberta.seekaride.core;

import io.searchbox.annotations.JestId;

public class Review {

    private String description;
    private int rating;
    private String userID;
    @JestId
    private String id;

    public Review(String description, int rating, String userID) {
        this.description = description;
        this.rating = rating;
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }


}
