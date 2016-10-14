package com.c301t19.cs.ualberta.seekaride;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class Driver extends User {
    public boolean search(String s) {
        return true;
    }

    public void accept(RiderRequest request) {
    }

    public boolean isCompleted() {
        return true;
    }

    public ArrayList<RiderRequest> getList() {
        return new ArrayList<RiderRequest>();
    }

    public void beAccepted() {
    }

    public void reconnect() {
    }
}
