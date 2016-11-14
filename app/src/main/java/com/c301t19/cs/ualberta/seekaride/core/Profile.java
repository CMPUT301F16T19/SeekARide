package com.c301t19.cs.ualberta.seekaride.core;

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

    public Profile(String u, String p, String e) {
        username = u;
        phoneNumber = p;
        email = e;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getId() { return id; }

    public void setUsername(String name) {
        username = name;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

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
}
