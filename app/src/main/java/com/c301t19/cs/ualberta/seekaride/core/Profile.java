package com.c301t19.cs.ualberta.seekaride.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.annotations.JestId;

/**
 * Holds the data associated with a user's profile. Profiles are stored in an external database (Elasticsearch).
 */
public class Profile {

    private String username;
    private String phoneNumber;
    private String email;
    private String car;
    @JestId
    protected String id;

    /**
     * Instantiates a new Profile.
     *
     * @param username
     * @param phoneNumber
     * @param email
     * @param car
     */
    public Profile(String username, String phoneNumber, String email, String car) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.car = car;
    }

    /**
     * constructor
     * @param username
     * @param phoneNumber
     * @param email
     */
    @Deprecated
    public Profile(String username, String phoneNumber, String email) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.car = null;
    }

    /**
     * constructor
     * @param newProfile
     */
    public Profile(Profile newProfile) {
        this.username = newProfile.getUsername();
        this.phoneNumber = newProfile.getPhoneNumber();
        this.email = newProfile.getEmail();
        this.id = newProfile.getId();
        this.car = newProfile.getCar();
    }

    /**
     * get car
     * @return car
     */
    public String getCar() {
        if (car == null) {
            return "";
        }
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {

        if (id == null) {
            Log.i("ID", "was null");
            ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(ElasticsearchController.UserField.NAME, username);
            getUserTask.execute();
            try {
                String result = getUserTask.get().getId();
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    /**
     * Sets username.
     *
     * @param name the name
     */
    public void setUsername(String name) {
        username = name;
    }

    /**
     * Sets phone number.
     *
     * @param number the number
     */
    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    /**
     * Sets email.
     *
     * @param e the e
     */
    public void setEmail(String e) {
        email = e;
    }

    /**
     * compare 2 object
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        //changed from if statement.
        return ((Profile) (obj)).getId().equals(id);
    }

    /**
     * get the list of reviews
     */
    public ArrayList<Review> getReviews() {
        ElasticsearchController.GetReviewsTask getReviewsTask = new ElasticsearchController.GetReviewsTask(id);
        getReviewsTask.execute();
        try {
            return getReviewsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<Review>();
    }

    /**
     * get the average rating
     * @return rating
     */
    public float getRating() {
        ArrayList<Review> reviews = getReviews();
        float res = 0;
        if (reviews == null || reviews.isEmpty()) {
            return res;
        }
        for (int i = 0; i < reviews.size(); i++) {
            res = res + reviews.get(i).getRating();
        }
        return (res/reviews.size());
    }
}
