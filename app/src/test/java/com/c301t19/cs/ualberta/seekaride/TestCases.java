package com.c301t19.cs.ualberta.seekaride;

import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.c301t19.cs.ualberta.seekaride.core.User;

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

    //    Requests
    //
    //    US 01.01.01
    //    As a rider, I want to request rides between two locations.
    @Test
    public void testRequest1(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        // add the reqeust in the elastic search
        assertFalse(new Request("lol trip",startPoint,destination,price,userProfile).equals(rider.getRequest(0)));
        assertEquals(rider.getRequest(0).getRiderProfile(),userProfile);
    }

    //    US 01.02.01
    //    As a rider, I want to see current requests I have open.
    public void testRequest2(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertFalse(new Request("lol trip",startPoint,destination,price,userProfile).equals(rider.getRequest(0)));
        assertEquals(rider.getRequest(0).getRiderProfile(),userProfile);
    }

    //            US 01.03.01
    //    As a rider, I want to be notified if my request is accepted.
    public void testRequest3(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertFalse(new Request("lol trip",startPoint,destination,price,userProfile).equals(rider.getRequest(0)));
        assertEquals(rider.getRequest(0).getRiderProfile(),userProfile);

        Request testRequest = rider.getRequest(0);
        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);

        // since the object is passing by reference all the time, maybe we dont need to have update file
        assertEquals(rider.getRequest(0),testRequest);
        // in the activity, after we make the request, it will be in the waiting screen and use elascity search to check it
        // and update the request every time we check, may be after we found something, we can slow the clicking rate
        ArrayList<Request> testRequestList = new ArrayList<Request>();
        testRequestList.add(testRequest);
        rider.updateOpenRequests(testRequestList);
        // in the activity, the view of quest should be changed
        assertEquals(rider.getOpenRequests(),testRequestList);

    }


    //
    //    US 01.04.01
    //    As a rider, I want to cancel requests.
    public void testRequest4(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        assertFalse(new Request("lol trip",startPoint,destination,price,userProfile).equals(rider.getRequest(0)));
        assertEquals(rider.getRequest(0).getRiderProfile(),userProfile);
        // use rider.getCurrentRequests() get the request and find it in the elastic search and remove it, then delete it in t
        rider.deleteRequest(0);
        assertFalse(rider.hasRequests());
    }

    //
    //            US 01.05.01
    //    As a rider, I want to be able to phone or email the driver who accepted a request.
    public void testRequest5(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        ArrayList<Request> testRequests = rider.getOpenRequests();

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequests.get(0).driverAccepted(driverProfile);

        rider.updateOpenRequests(testRequests);

        rider.contactByPhone(rider.getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber());
        assertEquals(rider.getRequest(0).getAcceptedDriverProfiles().get(0).getPhoneNumber(),"0010010010");
    }

    //
    //            US 01.06.01
    //    As a rider, I want an estimate of a fair fare to offer to drivers.
    public void testRequest6(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");

        // some method to generate a remmcond price
        float price = rider.getRecommendedPrice(startPoint,destination);
        rider.makeRequest("lol trip", startPoint, destination, price);
    }


    //
    //    US 01.07.01
    //    As a rider, I want to confirm the completion of a request and enable payment.
    public void testRequest7(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);

        rider.completeRequest(0);
        // for current request
        rider.makePayment(0);
        // not sure how to let driver receive it

        assertTrue(rider.getRequest(0).isCompleted());

    }

    //
    //    US 01.08.01
    //    As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    public void testRequest8(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        testRequest.driverAccepted(driverProfile);
        //rider.updateRequest(testRequest);
        Profile driverProfile2 = new Profile("raichu","0020020020","raichu@pokemon.com");
        testRequest.driverAccepted(driverProfile2);
        //rider.updateRequest(testRequest);

        // accept pikachu
        rider.acceptDriverOffer(0,0);
        assertEquals(driverProfile,rider.getRequest(0).getDriverProfile());
        rider.acceptDriverOffer(0,1);
        assertEquals(driverProfile2,rider.getRequest(0).getDriverProfile());
    }

    //
    //
    //    Status
    //
    //    US 02.01.01
    //    As a rider or driver, I want to see the status of a request that I am involved in
    public void testRequest9(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        Driver driver = new Driver(driverProfile);
        driver.acceptRequest(testRequest);
        //testRequest.driverAccepted(driverProfile);
        //rider.updateRequest(testRequest);

        rider.getRequest(0); // returns the request info
        driver.getCurrentRequest(); // returns the request info

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

        assertTrue(userProfile==user.getProfile());
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
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Driver driver = new Driver(userProfile);
        // does nothing right now, wait for geo location
        driver.searchRequestsByLocation(new Location("111st"));
    }

    //
    //            US 04.02.01
    //    As a driver, I want to browse and search for open requests by keyword.
    public void testRequest14(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Driver driver = new Driver(userProfile);
        // does nothing right now, wait for geo location
        // driver.searchRequestsByKeyword("111st");
    }

    //
    //    Accepting
    //    US 05.01.01
    //    As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
    public void testRequest15(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        Driver driver = new Driver(driverProfile);
        driver.acceptRequest(testRequest);

        rider.acceptDriverOffer(0,0);
        rider.getRequest(0).complete();
        rider.makePayment(0);

        if(testRequest.isPaid()){
            driver.receivePayment();
        }
        assertFalse(driver.getCurrentRequest().isCompleted());

    }

    //
    //    US 05.02.01
    //    As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
    public void testRequest16(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getRequest(0);

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        Driver driver = new Driver(driverProfile);

        // BY elastic search and looking at the location, we can then search by elastic search, and return an ArrayList of OpenRequests
    }
    /*
    //
    //            US 05.03.01
    //    As a driver, I want to see if my acceptance was accepted.
    public void request17(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.setRequest(request);
        ArrayList<Request> requestList= driver.getList();
        assertFalse(requestList.get(0) == request);
        driver.accept(request);
        assertFalse(driver.isCompleted());
    }
    //
    //    US 05.04.01
    //    As a driver, I want to be notified if my ride offer was accepted.
    public void request18(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.setRequest(request);
        ArrayList<Request> requestList= driver.getList();
        assertFalse(requestList.get(0) == request);
        driver.accept(request);
        assertFalse(driver.isCompleted());
        driver.beAccepted();
        assertFalse(driver.isCompleted());
    }
    //
    //
    //
    //            Offline behavior
    //    US 08.01.01
    //    As an driver, I want to see requests that I already accepted while offline.
    public void request19(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.setRequest(request);
        ArrayList<Request> requestList= driver.getList();
        assertFalse(requestList.get(0) == request);
        driver.accept(request);
        assertFalse(driver.isCompleted());
        driver.beAccepted();
        assertFalse(driver.isCompleted());

        assertTrue(request == driver.getRequest());
    }
    //
    //            US 08.02.01
    //    As a rider, I want to see requests that I have made while offline.
    public void request20(){
        Rider rider = new Rider();
        Driver driver = new Driver();
        Request request = new Request();
        rider.setRequest(request);
        driver.setRequest(request);
        assertTrue(request == rider.getRequest());
        assertTrue(request == driver.getRequest());
    }
    //
    //            US 08.03.01
    //    As a rider, I want to make requests that will be sent once I get connectivity again.
    public void request21(){
        Rider rider = new Rider();
        Driver driver = new Driver();
        Request request = new Request();
        rider.setRequest(request);
        driver.setRequest(request);
        assertTrue(request == rider.getRequest());
        assertTrue(request == driver.getRequest());
        rider.reconnect();
        assertTrue(request == rider.getRequest());
        assertTrue(request == driver.getRequest());
    }
    //
    //
    //    US 08.04.01
    //    As a driver, I want to accept requests that will be sent once I get connectivity again.
    public void request22(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.setRequest(request);
        ArrayList<Request> requestList= driver.getList();
        assertFalse(requestList.get(0) == request);
        driver.accept(request);
        assertFalse(driver.isCompleted());
        driver.beAccepted();
        assertFalse(driver.isCompleted());

        assertTrue(request == driver.getRequest());
        driver.reconnect();
        assertFalse(driver.isCompleted());

        assertTrue(request == driver.getRequest());
    }
    //
    //
    //
    //    Location
    //    US 10.01.01
    //    As a rider, I want to specify a start and end geo locations on a map for a request.
    public void request23(){

        Rider rider = new Rider();
        String location1 = "111st";
        String location2 = "112st";
        assertTrue(rider.search(location1));
        assertTrue(rider.request(location1,location2));

    }
    //
    //    US 10.02.01 (added 2016-02-29)
    //
    //    As a driver, I want to view start and end geo locations on a map for a request.
    public void request24(){

        Driver driver = new Driver();
        String location1 = "111st";
        String location2 = "112st";
        assertTrue(driver.search(location1));

    }
*/
}
