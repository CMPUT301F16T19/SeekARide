package com.c301t19.cs.ualberta.seekaride;

import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.User;
import com.c301t19.cs.ualberta.seekaride.mock.MockDriver;
import com.c301t19.cs.ualberta.seekaride.mock.MockElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.mock.MockLocation;
import com.c301t19.cs.ualberta.seekaride.mock.MockNetworkManager;
import com.c301t19.cs.ualberta.seekaride.mock.MockProfile;
import com.c301t19.cs.ualberta.seekaride.mock.MockRequest;
import com.c301t19.cs.ualberta.seekaride.mock.MockRider;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class TestCases extends TestCase {

    public TestCases(){
        super();
    }

    MockProfile riderProfile;
    MockProfile driverProfile;

    @Override
    protected void setUp() {
        riderProfile = new MockProfile("AAAAAA","9989989988","mqu@ualberta.ca", "car", "id1");
        MockRider.instantiate(riderProfile);
        driverProfile = new MockProfile("BBBBBB","0010010010","pikachu@pokemon.com","very good car", "id2");
        MockDriver.instantiate(driverProfile);
        MockNetworkManager.instantiate(NetworkManager.Connectivity.MOBILE);
        MockElasticsearchController.instantiate();
    }

    //    Requests
    //    US 01.01.01
    //    As a rider, I want to request rides between two locations.
    @Test
    public void testRequest1() {

        MockLocation startPoint = new MockLocation("111st", 100, 100);
        MockLocation destination = new MockLocation("112st", 200, 200);
        float price = 998;
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
    }

    //    US 01.02.01
    //    As a rider, I want to see current requests I have open.
    public void testRequest2(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = 998;
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
    }

    //            US 01.03.01
    //    As a rider, I want to be notified if my request is accepted.
    public void testRequest3(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = 998;
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
        
        request.driverAccepted(driverProfile);

        // since the object is passing by reference all the time, maybe we dont need to have update file
        //assertEquals(Rider.getInstance().getRequest(0),testRequest);
        // in the activity, after we make the request, it will be in the waiting screen and use elascity search to check it
        // and update the request every time we check, may be after we found something, we can slow the clicking rate
        ArrayList<Request> testRequestList = new ArrayList<Request>();
        testRequestList.add(request);
        MockRider.getInstance().updateOpenRequests();
        // in the activity, the view of quest should be changed
        assertEquals(MockRider.getInstance().getOpenRequests(),testRequestList);
    }


    //
    //    US 01.04.01
    //    As a rider, I want to cancel requests.
    public void testRequest4(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = 998;
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
        // use rider.getCurrentRequests() get the request and find it in the elastic search and remove it, then delete it in t
        MockRider.getInstance().deleteRequest(0);

        assertFalse(MockRider.getInstance().hasRequests());
    }

    //
    //            US 01.05.01
    //    As a rider, I want to be able to phone or email the driver who accepted a request.
    public void testRequest5(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = 998;
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        
        MockRider.getInstance().getOpenRequests().get(0).driverAccepted(driverProfile);

        MockRider.getInstance().updateOpenRequests();

        MockRider.getInstance().contactByPhone(MockRider.getInstance().getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber());
        assertEquals(MockRider.getInstance().getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber(),"0010010010");
    }

    //
    //            US 01.06.01
    //    As a rider, I want an estimate of a fair fare to offer to drivers.
    public void testRequest6(){

        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

    }


    //
    //    US 01.07.01
    //    As a rider, I want to confirm the completion of a request and enable payment.
    public void testRequest7(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
        
        request.driverAccepted(driverProfile);

        MockRider.getInstance().completeRequest(0);
        // for current request
        MockRider.getInstance().makePayment(0);
        // not sure how to let driver receive it

        assertTrue(MockRider.getInstance().getRequest(0).isCompleted());

    }

    //
    //    US 01.08.01
    //    As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    public void testRequest8(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        
        MockProfile driverProfile2 = new MockProfile("raichu","0020020020","raichu@pokemon.com","lol car","id444");
        request.driverAccepted(driverProfile2);
        // accept pikachu
        MockRider.getInstance().acceptDriverOffer(0,0);
        assertEquals(driverProfile.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
        MockRider.getInstance().acceptDriverOffer(0,1);
        assertEquals(driverProfile2.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
    }

    //
    //
    //    Status
    //
    //    US 02.01.01
    //    As a rider or driver, I want to see the status of a request that I am involved in
    public void testRequest9(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        MockDriver.getInstance().acceptRequest(request);

        MockRider.getInstance().getRequest(0); // returns the request info
        MockDriver.getInstance().getAcceptedRequests(); // returns the request info
    }

    //
    //    User profile
    //
    //    US 03.01.01
    //    As a user, I want a profile with a unique username and my contact information.
    public void testRequest10(){
        // when create it, we have to check is that unique or not by elastic search
        Profile userProfile = new Profile("AAAAAA","9989989988","mqu@ualberta.ca");
        User user = new User(userProfile);

        assertEquals(MockRider.getInstance().getProfile().getUsername(),userProfile.getUsername());
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
        assertEquals(userProfile.getPhoneNumber(),"9989989988");
    }

    //
    //            Searching
    //
    //    US 04.01.01
    //    As a driver, I want to browse and search for open requests by geo-location.
    public void testRequest13(){

        MockDriver.getInstance().searchRequestsByLocation(new Location("111st"),50);
    }

    //
    //            US 04.02.01
    //    As a driver, I want to browse and search for open requests by keyword.
    public void testRequest14(){
        MockDriver.getInstance().searchRequestsByKeyword("university","50m");

    }

    //
    //    Accepting
    //    US 05.01.01
    //    As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
    public void testRequest15(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        
        MockRider.getInstance().acceptDriverOffer(0,0);
        MockRider.getInstance().getRequest(0).complete();
        MockRider.getInstance().makePayment(0);
        MockDriver.getInstance().acceptRequest(request);

        if(request.didRiderPay()){
            MockDriver.getInstance().receivePayment(0);
        }
        assertTrue(MockDriver.getInstance().getAcceptedRequests().get(0).isCompleted());
    }

    //
    //    US 05.02.01
    //    As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
    public void testRequest16(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
        // requests is the request list for driver to look at

    }

    //
    //            US 05.03.01
    //    As a driver, I want to see if my acceptance was accepted.
    public void testRequest17(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        
        // accept pikachu
        MockRider.getInstance().acceptDriverOffer(0,0);
        assertEquals(driverProfile.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
        
    }

    //
    //    US 05.04.01
    //    As a driver, I want to be notified if my ride offer was accepted.
    public void testRequest18(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);

        // accept pikachu
        MockRider.getInstance().acceptDriverOffer(0,0);
        assertEquals(driverProfile.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
        MockDriver.getInstance().hasReceivedNotification();

    }

    //
    //
    //
    //            Offline behavior
    //    US 08.01.01
    //    As an driver, I want to see requests that I already accepted while offline.
    public void testRequest19(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        MockDriver.getInstance().acceptRequest(request);
        assertFalse(MockDriver.getInstance().isConfirmed(0));

    }

    //
    //            US 08.02.01
    //    As a rider, I want to see requests that I have made while offline.
    public void testRequest20(){

        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

    }

    //
    //            US 08.03.01
    //    As a rider, I want to make requests that will be sent once I get connectivity again.
    public void testRequest21(){

        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        MockDriver.getInstance().acceptRequest(request);
        assertFalse(MockDriver.getInstance().isConfirmed(0));


    }

    //
    //
    //    US 08.04.01
    //    As a driver, I want to accept requests that will be sent once I get connectivity again.
    public void testRequest22(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        MockDriver.getInstance().acceptRequest(request);
        
        assertFalse(MockDriver.getInstance().isConfirmed(0));
    }

    //
    //
    //
    //    Location
    //    US 10.01.01
    //    As a rider, I want to specify a start and end geo locations on a map for a request.
    public void testRequest23(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
    }

    //
    //    US 10.02.01 (added 2016-02-29)
    //
    //    As a driver, I want to view start and end geo locations on a map for a request.
    public void testRequest24(){

        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));
        
        assertEquals(request.getStart(),startPoint);
        assertEquals(request.getDestination(),destination);
    }

//    US 1.09.01 (added 2016-11-14)
//    As a rider, I should see a description of the driver's vehicle.
    public void testRequest25(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        MockDriver.getInstance().acceptRequest(request);
        request.driverAccepted(driverProfile);
        assertFalse(MockDriver.getInstance().isConfirmed(0));
        
        assertEquals(request.getAcceptedDriverProfiles().get(0).getCar(),"very good car");
    }

//    US 1.10.01 (added 2016-11-14)
//    As a rider, I want to see some summary rating of the drivers who accepted my offers.
    public void testRequest26(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);

        MockProfile driverProfile2 = new MockProfile("raichu","0020020020","raichu@pokemon.com","lol car","id444");
        request.driverAccepted(driverProfile2);
        // accept pikachu
        MockRider.getInstance().acceptDriverOffer(0,0);
        assertEquals(driverProfile.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());
        MockRider.getInstance().acceptDriverOffer(0,1);
        assertEquals(driverProfile2.getUsername(),MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getUsername());


        MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getRating();
        MockRider.getInstance().getRequest(0).getAcceptedDriverProfile().getReviews();
    }

//    US 1.11.01 (added 2016-11-14)
//    As a rider, I want to rate a driver for his/her service (1-5).
    public void testRequest27(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);

        MockRider.getInstance().acceptDriverOffer(0,0);
        MockRider.getInstance().getRequest(0).complete();
        MockRider.getInstance().makePayment(0);
        MockDriver.getInstance().acceptRequest(request);

        if(request.didRiderPay()){
            MockDriver.getInstance().receivePayment(0);
        }
        assertTrue(MockDriver.getInstance().getAcceptedRequests().get(0).isCompleted());
        String descrip = "not bad";
        int rating = 4;
        Review review = new Review(descrip,rating,MockDriver.getInstance().getProfile().getId());
        MockRider.getInstance().leaveReview(review);

        ArrayList<Review> reviews= MockDriver.getInstance().getProfile().getReviews();
        assertTrue(reviews.contains(review));
        assertEquals(review.getRating(),MockDriver.getInstance().getProfile().getReviews().get(0).getRating());

    }

//
//    US 03.04.01 (added 2016-11-14)
//
//    As a driver, in my profile I can provide details about the vehicle I drive.
    public void testRequest28(){
        assertEquals(MockDriver.getInstance().getProfile().getCar(),"very good car");
    }
//
//    US 04.03.01 (added 2016-11-14)
//    As a driver, I should be able filter request searches by price per KM and price.
    public void testRequest29(){
        MockLocation startPoint = new MockLocation("111st",100,100);
        MockLocation destination = new MockLocation("112st",200,200);
        float price = MockRider.getInstance().getRecommendedPrice(startPoint,destination);
        MockRequest request = MockRider.getInstance().makeRequest("lol trip", startPoint, destination, price, "id111");
        ArrayList<Request> requests = MockElasticsearchController.GetRequestsTask(ElasticsearchController.RequestField.ID, "id111");
        assertTrue(requests.contains(request));

        request.driverAccepted(driverProfile);
        MockDriver.getInstance().getSearchedRequests();
        request.getPricePerKm();

    }

}