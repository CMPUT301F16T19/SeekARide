package com.c301t19.cs.ualberta.seekaride.mock;

import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Review;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.annotations.JestId;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockProfile extends Profile{

    /**
     * Instantiates a new Profile.
     *
     * @param u the u
     * @param p the p
     * @param e the e
     */
    public MockProfile(String u, String p, String e, String c, String id) {
        super(u,p,e,c);
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    @Override
    public String getId() {

        return id;
    }

    @Override
    public ArrayList<Review> getReviews() {
        return new ArrayList<Review>();
    }

    @Override
    public float getRating() {
        return 0;
    }
}

