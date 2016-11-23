package com.c301t19.cs.ualberta.seekaride;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.c301t19.cs.ualberta.seekaride.core.User;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by mc on 16/10/13.
 */
public class TestCases extends TestCase {

    public TestCases(){
        super();
    }

    Profile userProfile;
    Profile driverProfile;

    @Override
    protected void setUp() {
        userProfile = new Profile("AAAAAA","9989989988","mqu@ualberta.ca");
        Rider.instantiate(userProfile);
        driverProfile = new Profile("BBBBBB","0010010010","pikachu@pokemon.com","very good car");
        Driver.instantiate(driverProfile);
    }

    //    Requests
    //    US 01.01.01
    //    As a rider, I want to request rides between two locations.
    @Test
    public void testRequest1() {

        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Request request = Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        ElasticsearchController.GetRequestsTask getRequestsTask = new ElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.RIDERID, userProfile.getId());
        getRequestsTask.execute();
        ArrayList<Request> requests = null;
        try {
            requests = getRequestsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assertTrue(requests.contains(request));
    }

    //    US 01.02.01
    //    As a rider, I want to see current requests I have open.
    public void testRequest2(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Request request = Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        Rider.getInstance().updateOpenRequests();
        assertTrue(Rider.getInstance().getOpenRequests().contains(request));
    }

    //            US 01.03.01
    //    As a rider, I want to be notified if my request is accepted.
    public void testRequest3(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());

        Request testRequest = Rider.getInstance().getRequest(0);
        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);

        // since the object is passing by reference all the time, maybe we dont need to have update file
        //assertEquals(Rider.getInstance().getRequest(0),testRequest);
        // in the activity, after we make the request, it will be in the waiting screen and use elascity search to check it
        // and update the request every time we check, may be after we found something, we can slow the clicking rate
        ArrayList<Request> testRequestList = new ArrayList<Request>();
        testRequestList.add(testRequest);
        Rider.getInstance().updateOpenRequests();
        // in the activity, the view of quest should be changed
        //assertEquals(Rider.getInstance().getOpenRequests(),testRequestList);

    }


    //
    //    US 01.04.01
    //    As a rider, I want to cancel requests.
    public void testRequest4(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        // use rider.getCurrentRequests() get the request and find it in the elastic search and remove it, then delete it in t
        Rider.getInstance().deleteRequest(0);

        //assertFalse(Rider.getInstance().hasRequests());
    }

    //
    //            US 01.05.01
    //    As a rider, I want to be able to phone or email the driver who accepted a request.
    public void testRequest5(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());


        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        Rider.getInstance().getOpenRequests().get(0).driverAccepted(driverProfile);

        //Rider.getInstance().updateOpenRequests();

        Rider.getInstance().contactByPhone(Rider.getInstance().getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber());
        assertEquals(Rider.getInstance().getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber(),"0010010010");
    }

    //
    //            US 01.06.01
    //    As a rider, I want an estimate of a fair fare to offer to drivers.
    public void testRequest6(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = Rider.getInstance().getRecommendedPrice(startPoint,destination);
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());

    }


    //
    //    US 01.07.01
    //    As a rider, I want to confirm the completion of a request and enable payment.
    public void testRequest7(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());

        Request testRequest = Rider.getInstance().getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);

        Rider.getInstance().completeRequest(0);
        // for current request
        Rider.getInstance().makePayment(0);
        // not sure how to let driver receive it

        assertTrue(Rider.getInstance().getRequest(0).isCompleted());

    }

    //
    //    US 01.08.01
    //    As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    public void testRequest8(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        testRequest.driverAccepted(driverProfile);
        //rider.updateRequest(testRequest);
        Profile driverProfile2 = new Profile("raichu","0020020020","raichu@pokemon.com");
        testRequest.driverAccepted(driverProfile2);
        //rider.updateRequest(testRequest);

        // accept pikachu
        Rider.getInstance().acceptDriverOffer(0,0);
        assertEquals(driverProfile.getUsername(),Rider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
        Rider.getInstance().acceptDriverOffer(0,1);
        assertEquals(driverProfile2.getUsername(),Rider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
    }

    //
    //
    //    Status
    //
    //    US 02.01.01
    //    As a rider or driver, I want to see the status of a request that I am involved in
    public void testRequest9(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        //testRequest.driverAccepted(driverProfile);
        //rider.updateRequest(testRequest);

        Rider.getInstance().getRequest(0); // returns the request info
        Driver.getInstance().getAcceptedRequests(); // returns the request info
    }

    //
    //    User profile
    //
    //    US 03.01.01
    //    As a user, I want a profile with a unique username and my contact information.
    public void testRequest10(){
        // when create it, we have to check is that unique or not by elastic search
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        User user = new User(userProfile);

        assertEquals(Rider.getInstance().getProfile().getUsername(),userProfile.getUsername());
    }

    //
    //    US 03.02.01
    //    As a user, I want to edit the contact information in my profile.
    public void testRequest11(){
        // when create it, we have to check is that unique or not by elastic search
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        User user = new User(userProfile);
        // get the info from user
        Profile newProfile = new Profile("MC","9989989988","mqu@ualberta.ca");
        user.setProfile(newProfile);
        assertTrue(newProfile==user.getProfile());
    }

    //
    //    US 03.03.01
    //    As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.

    public void testRequest12(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        User user = new User(userProfile);
        String username = "mc";
        // have to use elastic search not working yet
        user.searchPhone(username);
    }

    //
    //            Searching
    //
    //    US 04.01.01
    //    As a driver, I want to browse and search for open requests by geo-location.
    public void testRequest13(){

        Driver.getInstance().searchRequestsByLocation(new Location("111st"),"50m");
    }

    //
    //            US 04.02.01
    //    As a driver, I want to browse and search for open requests by keyword.
    public void testRequest14(){
        Driver.getInstance().searchRequestsByKeyword("university","50m");

    }

    //
    //    Accepting
    //    US 05.01.01
    //    As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
    public void testRequest15(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);

        Rider.getInstance().acceptDriverOffer(0,0);
        Rider.getInstance().getRequest(0).complete();
        Rider.getInstance().makePayment(0);

        if(testRequest.didRiderPay()){
            Driver.getInstance().receivePayment(0);
        }
        //assertTrue(Driver.getInstance().getAcceptedRequests().get(0).isCompleted());
    }

    //
    //    US 05.02.01
    //    As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
    public void testRequest16(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);

    }

    //
    //            US 05.03.01
    //    As a driver, I want to see if my acceptance was accepted.
    public void testRequest17(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));
    }

    //
    //    US 05.04.01
    //    As a driver, I want to be notified if my ride offer was accepted.
    public void testRequest18(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

    }

    //
    //
    //
    //            Offline behavior
    //    US 08.01.01
    //    As an driver, I want to see requests that I already accepted while offline.
    public void testRequest19(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

        Driver.getInstance().getAcceptedRequests();
    }

    //
    //            US 08.02.01
    //    As a rider, I want to see requests that I have made while offline.
    public void testRequest20(){

        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);

        ArrayList<Request> testRequests = Rider.getInstance().getOpenRequests();


    }

    //
    //            US 08.03.01
    //    As a rider, I want to make requests that will be sent once I get connectivity again.
    public void testRequest21(){

        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

    }

    //
    //
    //    US 08.04.01
    //    As a driver, I want to accept requests that will be sent once I get connectivity again.
    public void testRequest22(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;

        // should check it connects or not here
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
    }

    //
    //
    //
    //    Location
    //    US 10.01.01
    //    As a rider, I want to specify a start and end geo locations on a map for a request.
    public void testRequest23(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());

    }

    //
    //    US 10.02.01 (added 2016-02-29)
    //
    //    As a driver, I want to view start and end geo locations on a map for a request.
    public void testRequest24(){

        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));


        Driver.getInstance().getAcceptedRequests().get(0).getStart();
        Driver.getInstance().getAcceptedRequests().get(0).getDestination();
    }

//    US 1.09.01 (added 2016-11-14)
//    As a rider, I should see a description of the driver's vehicle.
    public void testRequest25(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

        Rider.getInstance().getRequest(0).getAcceptedDriverProfile().getCar();
    }

//    US 1.10.01 (added 2016-11-14)
//    As a rider, I want to see some summary rating of the drivers who accepted my offers.
    public void testRequest26(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

        //Rider.getInstance().getRequest(0).getAcceptedDriverProfile().getRating();
        //Rider.getInstance().getRequest(0).getAcceptedDriverProfile().getReviews();

    }

//    US 1.11.01 (added 2016-11-14)
//    As a rider, I want to rate a driver for his/her service (1-5).
    public void testRequest27(){
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        Rider.getInstance().makeRequest("lol trip", startPoint, destination, price);
        assertEquals(Rider.getInstance().getRequest(0).getRiderProfile().getUsername(),userProfile.getUsername());
        Request testRequest = Rider.getInstance().getRequest(0);


        Driver.getInstance().acceptRequest(testRequest);
        assertFalse(Driver.getInstance().isConfirmed(0));
        Rider.getInstance().acceptDriverOffer(0,0);
        //assertTrue(Driver.getInstance().isConfirmed(0));

        Rider.getInstance().completeRequest(0);
        String descrip = "not bad";
        int rating = 4;
        Review review = new Review(descrip,rating,Driver.getInstance().getProfile().getId());
        Rider.getInstance().leaveReview(review);

        ArrayList<Review> reviews= Driver.getInstance().getProfile().getReviews();
        //assertTrue(reviews.contains(review));

    }

//
//    US 03.04.01 (added 2016-11-14)
//
//    As a driver, in my profile I can provide details about the vehicle I drive.
    public void testRequest28(){
        assertEquals(Driver.getInstance().getProfile().getCar(),"very good car");
    }
//
//    US 04.03.01 (added 2016-11-14)
//    As a driver, I should be able filter request searches by price per KM and price.
    public void testRequest29(){
        //by elasticSearching,
    }
//
//            US 04.04.01 (added 2016-11-14)
//    As a driver, I should be able to see the addresses of the requests. ---- we will save it later on if we have time
//
//            US 04.05.01 (added 2016-11-14)
//
//    As a driver, I should be able to search by address or nearby an address. ---- we will save it later on if we have time
//


}