package com.deynek.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deynek.app.model.User;
import com.deynek.app.session.SessionManager;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    // Session Manager Class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Elements
        TextView labelUsername = (TextView) findViewById(R.id.labelUsername);
        TextView labelEmail = (TextView) findViewById(R.id.labelEmail);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);

        // Logout button click event
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // This will clear all session data and redirect user to LoginActivity
                session.logoutUser();
            }
        });

        // Session class instance
        session = new SessionManager(getApplicationContext());

        // This will redirect user to LoginActivity is he is not logged in
        session.checkLogin();
        if(session.isLoggedIn()){
            // IF LOGIN SUCCESSFUL:
            User loggedInUser = session.getUser();

            // displaying user data
            labelUsername.setText(Html.fromHtml("Name: <b>" + loggedInUser.getUsername() + "</b>"));
            labelEmail .setText(Html.fromHtml("Email: <b>" + loggedInUser.getEmail() + "</b>"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
