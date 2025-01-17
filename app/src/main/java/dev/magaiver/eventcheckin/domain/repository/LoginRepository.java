package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;

import dev.magaiver.eventcheckin.domain.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(Application application) {
    }

    public static LoginRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LoginRepository(application);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    public void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public LoggedInUser getUser() {
        return user;
    }
}