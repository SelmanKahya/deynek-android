package com.deynek.app.session;

import android.content.Context;
import android.content.Intent;

import com.deynek.app.activity.LoginActivity;
import com.deynek.app.model.MyApplication;
import com.deynek.app.model.User;
import com.deynek.app.service.API;
import com.deynek.app.util.PreferenceManager;

import static com.deynek.app.util.PreferenceManager.SHARED_PREFERENCES;

// Class implementation taken from:
// http://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
public class SessionManager {

    // Context
    private Context _context;

    // Logged in user object
    private static User user = null;

    // login result
    private static Boolean loginResult = false;

    private PreferenceManager pm;

    // Constructor
    public SessionManager(){
        _context = MyApplication.getAppContext();
        pm = new PreferenceManager(SHARED_PREFERENCES.SESSION);
    }

    public Boolean login(String username, String password){
        // validate user credentials
        if(API.validateUser(username, password)){
            user = new User(username, password, "test@example.com", "M");
            pm.setPreference("username", username);
            pm.setPreference("password", password);
            return true;
        }
        return false;
    }

    // This func will check pref if there is username and password set
    // if yes, it will use them to login, otherwise redirect to login
    public boolean checkLogin(){
        String storedUsername = pm.getPreference("username");
        String storedPassword = pm.getPreference("password");

        // check pref, is username and password is set, use them to login
        if(storedUsername != null && storedPassword != null){
            loginResult = this.login(storedUsername, storedPassword);
            if(!loginResult){
                redirectToLogin();
                return false;
            }
        }
        else{
            redirectToLogin();
            return false;
        }

        return true;
    }

    // Check if user is initialized
    public boolean isLoggedIn(){
        return loginResult;
    }

    // Clear session details
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        pm.clearPreference();

        // this will redirect user to login activity
        // since session is destroyed
        this.checkLogin();
    }

    public void redirectToLogin() {
        // user is not logged in redirect him to LoginActivity Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring LoginActivity Activity
        _context.startActivity(i);
    }

    // Returns logged in user object
    public User getUser(){
        return user;
    }
}