package com.c301t19.cs.ualberta.seekaride.core;

import android.widget.Toast;

/**
 * Controller that handles login and account creation.
 * <p/>
 * Issues: may want to implement a more rigorous account creation/login process in the future.
 */
public class LoginController {

    /**
     * Instantiates a new Login controller.
     */
    public LoginController () {

    }

    /**
     * Logs the user in with their desired username. A new account is created if a user doesn't already exist
     * with the given name. The name must be at least five characters long. Rider and Driver controller classes
     * are instantiated with a Profile generated from the username.
     *
     * @param username The user's username.
     * @return true if the login was successful.
     */
    public boolean login(String username) {
        if (username.length() <  5)
        {
            return false;
        }
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        Profile profile;
        try {
            profile = getUserTask.get();
            if (profile == null) {
                return false;
            }
            else {
                Rider.instantiate(profile);
                Driver.instantiate(profile);
            }
        }
        catch (Exception e) {

        }
        return true;
    }

    public boolean createNewAccount(Profile profile) {
        if (checkUserExists(profile.getUsername())) {
            return false;
        }
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(profile);
        addUserTask.execute();
        return true;
    }

    public void editAccount(Profile oldProfile, Profile newProfile) {
        ElasticsearchController.DeleteUserTask deleteUserTask = new ElasticsearchController.DeleteUserTask(oldProfile);
        ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask(newProfile, newProfile.getId());
        deleteUserTask.execute();
        addUserTask.execute();
        Rider.instantiate(newProfile);
        Driver.instantiate(newProfile);
    }

    private boolean checkUserExists(String username) {
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, username);
        getUserTask.execute();
        Profile profile;
        try {
            profile = getUserTask.get();
            if (profile == null) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception e) {

        }
        return true;
    }
}
