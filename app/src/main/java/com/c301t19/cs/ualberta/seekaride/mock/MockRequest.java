package com.c301t19.cs.ualberta.seekaride.mock;

import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;

import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Master Chief on 2016-11-25.
 */
public class MockRequest extends Request {

    /**
     * Instantiates a new Request.
     *
     * @param descrip      the descrip
     * @param st           the st
     * @param dest         the dest
     * @param pr           the pr
     * @param riderProfile the rider profile
     * @param riderId      the rider id
     */
    public MockRequest(String descrip, Location st, Location dest,
                   double pr,Profile riderProfile, String riderId, String requestId) {
        super(descrip,st,dest,pr,riderProfile,riderId);
        id = requestId;
    }

    public MockRequest(Request r) {
        super(r);
    }
}
