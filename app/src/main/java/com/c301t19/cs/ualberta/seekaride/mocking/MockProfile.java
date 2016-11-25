package com.c301t19.cs.ualberta.seekaride.mocking;

import com.c301t19.cs.ualberta.seekaride.core.Profile;

/**
 * Created by mc on 16/11/24.
 */
public class MockProfile extends Profile {

    private String id;

    public MockProfile(String username, String phone, String email, String car, String id) {
        super(username, phone, email, car);
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
