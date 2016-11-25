package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.Review;

import io.searchbox.annotations.JestId;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockReview extends Review {

    public MockReview(String description, float rating, String userID, String reviewID) {
        super(description,rating,userID);
        id = reviewID;
    }
}
