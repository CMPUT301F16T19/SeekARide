package com.c301t19.cs.ualberta.seekaride.core;

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
    private String id;

    /**
     * Instantiates a new Profile.
     *
     * @param u the u
     * @param p the p
     * @param e the e
     */
    public Profile(String u, String p, String e, String c) {
        username = u;
        phoneNumber = p;
        email = e;
        car = c;
    }

    public Profile(String u, String p, String e) {
        username = u;
        phoneNumber = p;
        email = e;
        car = null;
    }

    public Profile(Profile p) {
        username = p.getUsername();
        phoneNumber = p.getPhoneNumber();
        email = p.getEmail();
        id = p.getId();
        car = p.getCar();
    }

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
    public String getId() { return id; }

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

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        //changed from if statement.
        return ((Profile) (obj)).getId().equals(id);
    }

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

    public double getRating() {
        ArrayList<Review> reviews = getReviews();
        double res = 0;
        if (reviews == null || reviews.isEmpty()) {
            return res;
        }
        for (int i = 0; i < reviews.size(); i++) {
            res = res + reviews.get(i).getRating();
        }
        return (res/reviews.size());
    }
}
