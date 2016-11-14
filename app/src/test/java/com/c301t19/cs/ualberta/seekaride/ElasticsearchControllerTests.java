//package com.c301t19.cs.ualberta.seekaride;
//
//import android.content.Context;
//import android.test.ActivityInstrumentationTestCase2;
//import android.test.mock.MockContext;
//
//import com.c301t19.cs.ualberta.seekaride.activities.MainActivity;
//import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
//import com.c301t19.cs.ualberta.seekaride.core.Location;
//import com.c301t19.cs.ualberta.seekaride.core.Profile;
//import com.c301t19.cs.ualberta.seekaride.core.Request;
//
//import java.util.ArrayList;
//
//public class ElasticsearchControllerTests extends ActivityInstrumentationTestCase2 {
//
//    Profile riderProfile;
//    Request request;
//    Location start;
//    Location end;
//
//
//    public ElasticsearchControllerTests(){
//        super(MainActivity.class);
//    }
//
//    protected void setUp() throws Exception {
//        super.setUp();
//        start = new Location("start");
//        end = new Location("end");
//        riderProfile = new Profile("testName", "testPhone", "testEmail");
//        request = new Request("testRequest", start, end, 0.75, riderProfile);
//    }
//
//    public void testAddUser() {
//        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(riderProfile);
//        addUserTask.execute();
//        try {
//            assertTrue(addUserTask.get());
//        }
//        catch (Exception e) {
//        }
//    }
//
//    public void testGetUserByName() {
//        ElasticsearchController.GetUserTask getUserTaskName = new ElasticsearchController.GetUserTask(ElasticsearchController.UserField.NAME, "testName");
//        getUserTaskName.execute();
//        try {
//            assertTrue(getUserTaskName.get().getProfile().getEmail().equals(riderProfile.getEmail()));
//            assertTrue(getUserTaskName.get().getProfile().getPhoneNumber().equals(riderProfile.getPhoneNumber()));
//            assertTrue(getUserTaskName.get().getProfile().getUsername().equals(riderProfile.getUsername()));
//        }
//        catch (Exception e) {
//        }
//    }
//
//    // may test GetUserByEmail/Phone if the app requires this functionality
//
//    public void testEditUserEmail() {
//        String newEmail = "testEmailEdit";
//        ElasticsearchController.EditUserTask editUserTaskEmail = new ElasticsearchController.EditUserTask(ElasticsearchController.UserField.EMAIL, newEmail);
//        editUserTaskEmail.execute();
//        try {
//            assertTrue(editUserTaskEmail.get());
//        }
//        catch (Exception e) {
//
//        }
//    }
//
//    public void testEditUserPhone() {
//        String newPhone = "testPhoneEdit";
//        ElasticsearchController.EditUserTask editUserTaskPhone = new ElasticsearchController.EditUserTask(ElasticsearchController.UserField.PHONE, newPhone);
//        editUserTaskPhone.execute();
//        try {
//            assertTrue(editUserTaskPhone.get());
//        }
//        catch (Exception e) {
//
//        }
//    }
//
//    // may test EditUsername if the app requires this
//
//    public void testAddRequest() {
//        ElasticsearchController.AddRequestTask addRequestTask = new ElasticsearchController.AddRequestTask(request);
//        addRequestTask.execute();
//        try {
//            assertTrue(addRequestTask.get());
//        }
//        catch (Exception e) {
//
//        }
//    }
//
//    public void testGetRequest() {
//
//    }
//
//    public void testSearchRequestsKeyword() {
//        ArrayList<String> searchTerms = new ArrayList<String>();
//        searchTerms.add("testRequest");
//        ElasticsearchController.SearchRequestsByKeywordTask searchTask = new ElasticsearchController.SearchRequestsByKeywordTask(searchTerms);
//        searchTask.execute();
//        try {
//            assertTrue(searchTask.get().contains(request));
//        }
//        catch (Exception e) {
//
//        }
//    }
//
//    public void testSearchRequestsLocation() {
//        int radius = 10;
//        ElasticsearchController.SearchRequestsByLocationTask searchTask = new ElasticsearchController.SearchRequestsByLocationTask(start, radius);
//        searchTask.execute();
//        try {
//            assertTrue(searchTask.get().contains(request));
//        }
//        catch (Exception e) {
//
//        }
//    }
//
//    public void testSearchRequestsSimple() {
//
//    }
//
//    public void testEditRequest() {
//
//    }
//
//    public void testDeleteRequest() {
//
//    }
//}
