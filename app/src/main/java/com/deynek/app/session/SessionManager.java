package com.deynek.app.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.deynek.app.LoginActivity;
import com.deynek.app.model.User;

// Class implementation taken from:
// http://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Logged in user object
    private static User user = null;

    // Sharedpref file name
    private static final String PREF_NAME = "DeynekSessionPref";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public Boolean login(String username, String password){
        // Check if username, password is filled
        if(username.trim().length() > 0 && password.trim().length() > 0){
            if(username.equals("test") && password.equals("test")){
                user = new User("test@example.com", "test", "test@example.com", "M");
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                return true;

            } else{
                // username / password doesn't match
                return false;
            }
        }

        return false;
    }

    // This func will check pref if there is username and password set
    // if yes, it will use them to login, otherwise redirect to login
    public void checkLogin(){
        String storedUsername = pref.getString("username", null);
        String storedPassword = pref.getString("password", null);

        // check pref, is username and password is set, use them to login
        if(storedUsername != null && storedPassword != null){
            Boolean loginResult = this.login(storedUsername, storedPassword);
            if(!loginResult)
                redirectToLogin();
        }
        else
            redirectToLogin();
    }

    // Check if user is initialized
    public boolean isLoggedIn(){
        return user != null;
    }

    // Clear session details
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

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