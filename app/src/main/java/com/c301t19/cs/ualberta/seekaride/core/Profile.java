package com.c301t19.cs.ualberta.seekaride.core;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Holds the data associated with a user's profile. Profiles are stored in an external database (Elasticsearch).
 */
public class Profile {

    private String username;
    private String phoneNumber;
    private String email;
    @JestId
    private String id;

    private ArrayList<String> openRequests;
    private ArrayList<String> acceptedRequests;

    /**
     * Instantiates a new Profile.
     *
     * @param u the u
     * @param p the p
     * @param e the e
     */
    public Profile(String u, String p, String e) {
        username = u;
        phoneNumber = p;
        email = e;

        openRequests = new ArrayList<String>();
        acceptedRequests = new ArrayList<String>();
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
        if (((Profile)(obj)).getId().equals(id)) {
            return true;
        }
        return false;
    }


    public ArrayList<String> getOpenRequests() {
        if (openRequests == null) {
            setOpenRequests(new ArrayList<String>());
        }
        return openRequests;
    }

    public void setOpenRequests(ArrayList<String> openRequests) {
        this.openRequests = openRequests;
    }

    public ArrayList<String> getAcceptedRequests() {
        if (acceptedRequests == null) {
            setAcceptedRequests(new ArrayList<String>());
        }
        return acceptedRequests;
    }

    public void setAcceptedRequests(ArrayList<String> acceptedRequests) {
        this.acceptedRequests = acceptedRequests;
    }
}
