package com.c301t19.cs.ualberta.seekaride;

import android.test.ActivityInstrumentationTestCase2;

import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

import org.junit.Test;

/**
 * Created by mc on 16/10/13.
 */
public class RiderTests extends ActivityInstrumentationTestCase2 {
    public RiderTests() {
        super(com.c301t19.cs.ualberta.seekaride.activities.MainActivity.class);
    }

    //    Requests
    //
    //    US 01.01.01
    //    As a rider, I want to request rides between two locations.
    @Test
    public void testNothing() {
        assertTrue(false);
    }

    public void testRequest1() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        // add the reqeust in the elastic search
        assertTrue(new Request("lol trip", startPoint, destination, price, userProfile) == rider.getCurrentRequests());
        assertTrue(Boolean.TRUE);
        assertTrue(Boolean.FALSE);
    }

    //    US 01.02.01
    //    As a rider, I want to see current requests I have open.
    public void testRequest2() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertTrue(new Request("lol trip", startPoint, destination, price, userProfile) == rider.getCurrentRequests());

    }

    //            US 01.03.01
    //    As a rider, I want to be notified if my request is accepted.
    public void testRequest3() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertTrue(new Request("lol trip", startPoint, destination, price, userProfile) == rider.getCurrentRequests());
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu", "0010010010", "pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);
        // in the activity, after we make the request, it will be in the waiting screen and use elascity search to check it
        // and update the request every time we check, may be after we found something, we can slow the clicking rate
        rider.updateRequest(testRequest);
        // in the activity, the view of quest should be changed

    }


    //
    //    US 01.04.01
    //    As a rider, I want to cancel requests.
    public void testRequest4() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertTrue(new Request("lol trip", startPoint, destination, price, userProfile) == rider.getCurrentRequests());
        // use rider.getCurrentRequests() get the request and find it in the elastic search and remove it, then delete it in t
        rider.deleteRequest(0);
        assertTrue(rider.hasCurrentRequest());
    }

    //
    //            US 01.05.01
    //    As a rider, I want to be able to phone or email the driver who accepted a request.
    public void testRequest5() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu", "0010010010", "pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);
        rider.updateRequest(testRequest);

        rider.contactByPhone(rider.getCurrentRequests().getAcceptedDriverProfiles().get(0).getPhoneNumber());
        assertEquals(rider.getCurrentRequests().getAcceptedDriverProfiles().get(0).getPhoneNumber(), "0010010010");
    }

    //
    //            US 01.06.01
    //    As a rider, I want an estimate of a fair fare to offer to drivers.
    public void testRequest6() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");

        // some method to generate a remmcond price
        float price = rider.getRecommendedPrice(startPoint, destination);
        rider.makeRequest("lol trip", startPoint, destination, price);
    }


    //
    //    US 01.07.01
    //    As a rider, I want to confirm the completion of a request and enable payment.
    public void testRequest7() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu", "0010010010", "pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);
        rider.updateRequest(testRequest);

        rider.completeCurrentRequest();
        // for current request
        rider.makePayment();
        // not sure how to let driver receive it

        assertTrue(rider.getCurrentRequests().isCompleted());

    }

    //
    //    US 01.08.01
    //    As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    public void testRequest8() {
        Profile userProfile = new Profile("mc", "9989989988", "mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu", "0010010010", "pikachu@pokemon.com");
        testRequest.driverAccepted(driverProfile);
        rider.updateRequest(testRequest);
        Profile driverProfile2 = new Profile("raichu", "0020020020", "raichu@pokemon.com");
        testRequest.driverAccepted(driverProfile2);
        rider.updateRequest(testRequest);

        // accept pikachu
        rider.acceptDriverOffer(0);
        assertEquals(driverProfile, rider.getCurrentRequests().getDriverProfile());
    }
}