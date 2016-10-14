package com.c301t19.cs.ualberta.seekaride;

/**
 * Created by mc on 16/10/13.
 */
public class Rider extends User {
    public boolean request(String location1, String location2) {
        return true;
    }



    public boolean requestAccepted() {
        return true;
    }

    public void cancel() {
    }

    public boolean hasRequest() {
        return false;
    }

    public boolean phone() {
        return true;
    }

    public int estimate() {
        return 100;
    }

    public void comfirm() {

    }

    public boolean isCompleted() {
        return true;
    }

    public boolean pay(int estimate) {
        return true;
    }

    public void accept(RiderRequest request) {
    }

    public void reconnect() {
    }

    public boolean search(String location1) {
    }
}
