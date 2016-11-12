package com.c301t19.cs.ualberta.seekaride.core;

/**
 * Created by mc on 16/10/13.
 */
public class Profile {

    private String username;
    private String phoneNumber;
    private String email;

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

    public void setUsername(String name) {
        username = name;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    public void setEmail(String e) {
        email = e;
    }

}
