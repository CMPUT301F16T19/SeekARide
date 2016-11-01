package com.c301t19.cs.ualberta.seekaride;

import android.test.ActivityInstrumentationTestCase2;

import com.c301t19.cs.ualberta.seekaride.activities.MainActivity;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.User;

import java.util.ArrayList;

/**
 * Created by mc on 16/10/13.
 */
public class TestCases extends ActivityInstrumentationTestCase2 {
    public TestCases(){
        super(MainActivity.class);
    }
/*
    //    Requests
    //
    //    US 01.01.01
    //    As a rider, I want to request rides between two locations.
    public void request1(){

        Rider rider = new Rider();
        String location1 = "111st";
        String location2 = "112st";
        assertTrue(rider.request(location1,location2));
    }
    //    US 01.02.01
    //    As a rider, I want to see current requests I have open.
    public void request2(){
        Rider rider = new Rider();
        Request request = new Request();
        assertTrue(request == rider.getRequest());
    }

    //            US 01.03.01
    //    As a rider, I want to be notified if my request is accepted.
    public void request3(){
        Rider rider = new Rider();
        assertTrue(rider.requestAccepted());
    }
    //
    //    US 01.04.01
    //    As a rider, I want to cancel requests.
    public void request4(){
        Rider rider = new Rider();
        rider.cancel();
        assertTrue(rider.hasRequest());
    }
    //
    //            US 01.05.01
    //    As a rider, I want to be able to phone or email the driver who accepted a request.
    public void request5(){
        Rider rider = new Rider();
        assertTrue(rider.phone());
    }

    //
    //            US 01.06.01
    //    As a rider, I want an estimate of a fair fare to offer to drivers.
    public void request6(){
        Rider rider = new Rider();
        assertTrue(0 < rider.estimate());
    }
    //
    //    US 01.07.01
    //    As a rider, I want to confirm the completion of a request and enable payment.
    public void request7(){
        Rider rider = new Rider();
        rider.comfirm();
        assertTrue(rider.isCompleted());
        assertTrue(rider.pay(rider.estimate()));

    }
    //
    //    US 01.08.01
    //    As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    public void request8(){
        Rider rider = new Rider();
        Request request = new Request();

        rider.accept(request);
        assertFalse(rider.isCompleted());
    }
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
