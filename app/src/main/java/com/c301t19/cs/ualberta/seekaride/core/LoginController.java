package com.c301t19.cs.ualberta.seekaride.core;

public class LoginController {

    public LoginController () {

    }

    public boolean login(String username) {
        if (username.length() <  5)
        {
            return false;
        }
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        try {
            Profile profile = getUserTask.get();
            if (profile == null) {
                Profile newProfile = new Profile(username, "PHONE", "EMAIL");
                ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(newProfile);
                addUserTask.execute();
            }
            Rider.instantiate(profile);
            Driver.instantiate(profile);
        }
        catch (Exception e) {

        }
        return true;
    }
}
