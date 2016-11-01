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
public class TestCases extends ActivityInstrumentationTestCase2 {
    public TestCases(){
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
    public void testRequest1(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        // add the reqeust in the elastic search
        assertTrue(new Request("lol trip",startPoint,destination,price,userProfile)==rider.getCurrentRequests());
        assertTrue(Boolean.TRUE);
        assertTrue(Boolean.FALSE);
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
        assertTrue(new Request("lol trip",startPoint,destination,price,userProfile)==rider.getCurrentRequests());

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
        assertTrue(new Request("lol trip",startPoint,destination,price,userProfile)==rider.getCurrentRequests());
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);
        // in the activity, after we make the request, it will be in the waiting screen and use elascity search to check it
        // and update the request every time we check, may be after we found something, we can slow the clicking rate
        rider.updateRequest(testRequest);
        // in the activity, the view of quest should be changed

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
        assertTrue(new Request("lol trip",startPoint,destination,price,userProfile)==rider.getCurrentRequests());
        // use rider.getCurrentRequests() get the request and find it in the elastic search and remove it, then delete it in t
        rider.deleteRequest(0);
        assertTrue(rider.hasCurrentRequest());
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
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

        testRequest.driverAccepted(driverProfile);
        rider.updateRequest(testRequest);

        rider.contactByPhone(rider.getCurrentRequests().getAcceptedDriverProfiles().get(0).getPhoneNumber());
        assertEquals(rider.getCurrentRequests().getAcceptedDriverProfiles().get(0).getPhoneNumber(),"0010010010");
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
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");

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
    public void testRequest8(){
        Profile userProfile = new Profile("mc","9989989988","mqu@ualberta.ca");
        Rider rider = new Rider(userProfile);
        Location startPoint = new Location("111st");
        Location destination = new Location("112st");
        float price = 998;
        rider.makeRequest("lol trip", startPoint, destination, price);
        Request testRequest = rider.getCurrentRequests();

        Profile driverProfile = new Profile("pikachu","0010010010","pikachu@pokemon.com");
        testRequest.driverAccepted(driverProfile);
        rider.updateRequest(testRequest);
        Profile driverProfile2 = new Profile("raichu","0020020020","raichu@pokemon.com");
        testRequest.driverAccepted(driverProfile2);
        rider.updateRequest(testRequest);

        // accept pikachu
        rider.acceptDriverOffer(0);
        assertEquals(driverProfile,rider.getCurrentRequests().getDriverProfile());
    }
    /*
    //
    //
    //    Status
    //
    //    US 02.01.01
    //    As a rider or driver, I want to see the status of a request that I am involved in
    public void request9(){
        Rider rider = new Rider();
        Driver driver = new Driver();
        Request request = new Request();
        rider.setRequest(request);
        driver.setRequest(request);
        assertTrue(request == rider.getRequest());
        assertTrue(request == driver.getRequest());
    }
    //
    //    User profile
    //
    //    US 03.01.01
    //    As a user, I want a profile with a unique username and my contact information.
    public void request10(){
        User user = new User();
        Profile profile = new Profile();
        user.setProfile(profile);
        assertTrue(profile==user.getProfile());
    }
    //
    //    US 03.02.01
    //    As a user, I want to edit the contact information in my profile.
    public void request11(){
        User user = new User();
        Profile profile = new Profile();
        user.setProfile(profile);
        assertTrue(profile==user.getProfile());
        Profile profile2 = new Profile();
        profile2.setName("lol");
        user.setProfile(profile2);
        assertFalse(profile==user.getProfile());
        assertFalse(profile2==user.getProfile());
    }
    //
    //    US 03.03.01
    //    As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
    //    MC: not sure with this
    public void request12(){
        User user = new User();
        Profile profile = new Profile();
        user.setProfile(profile);
        assertTrue(profile==user.getProfile());
        Profile profile2 = new Profile();
        profile2.setName("lol");
        user.setProfile(profile2);
        assertFalse(profile==user.getProfile());
        assertFalse(profile2==user.getProfile());
    }
    //
    //            Searching
    //
    //    US 04.01.01
    //    As a driver, I want to browse and search for open requests by geo-location.
    public void request13(){
        Driver driver = new Driver();
        assertTrue(driver.search("111st"));
    }
    //
    //            US 04.02.01
    //    As a driver, I want to browse and search for open requests by keyword.
    public void request14(){
        Driver driver = new Driver();
        assertTrue(driver.search("uofa"));
    }
    //
    //    Accepting
    //    US 05.01.01
    //    As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
    public void request15(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.accept(request);
        assertFalse(driver.isCompleted());

    }
    //
    //    US 05.02.01
    //    As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
    public void request16(){
        Driver driver = new Driver();
        Request request = new Request();
        driver.setRequest(request);
        ArrayList<Request> requestList= driver.getList();
        assertFalse(requestList.get(0) == request);
    }
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
