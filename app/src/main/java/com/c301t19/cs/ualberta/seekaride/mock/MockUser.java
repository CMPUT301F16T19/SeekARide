package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.User;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockUser extends User {

    /**
     * Instantiates a new User.
     *
     * @param profile the profile
     */
    public MockUser(Profile profile) {
        super(profile);
    }

    @Override
    public void leaveReview(Review review) {
    }
}

