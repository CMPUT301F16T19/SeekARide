package com.c301t19.cs.ualberta.seekaride;


import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.mock.MockContext;

import com.c301t19.cs.ualberta.seekaride.activities.MainActivity;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;

public class ElasticsearchControllerTests extends ActivityInstrumentationTestCase2 {

    public ElasticsearchControllerTests(){
        super(MainActivity.class);
    }

    public void testAddUser() {
        Profile profile = new Profile("testName", "testPhone", "testEmail");
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(profile);
        addUserTask.execute();
        try {
            assertTrue(addUserTask.get());
        }
        catch (Exception e) {
        }
    }

    public void testGetUser() {

    }

    public void testEditUser() {

    }

    public void testAddRequest() {

    }

    public void testGetRequest() {

    }

    public void testSearchRequests() {

    }

    public void testSearchRequestsSimple() {

    }

    public void testEditRequest() {

    }

    public void testDeleteRequest() {

    }
}
