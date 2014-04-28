package com.deynek.app.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.deynek.app.activity.find.ArrivedActivity;
import com.deynek.app.activity.mark.LeavingSpotActivity;
import com.deynek.app.activity.account.LoginActivity;
import com.deynek.app.R;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.SessionManager;

public class MyActivity extends ActionBarActivity {

    private Activity act;
    private SessionManager session = new SessionManager();

    protected void onCreate(Bundle savedInstanceState, Activity act) {
        super.onCreate(savedInstanceState);
        this.act = act;
        // if not login activity, then check session
        if (!(act instanceof LoginActivity)) {
            session.checkLogin();
        }
    }

    @Override
    protected void onStart() {
        super.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(session.isLoggedIn()){
            checkAppState();
        }
    }

    public void checkAppState(){
        Intent i;
        ApplicationStateManager.STATES state = ApplicationStateManager.getState();

        // LEAVING
        if(state == ApplicationStateManager.STATES.LEAVING && !(act instanceof LeavingSpotActivity)){
            i = new Intent(getApplicationContext(), LeavingSpotActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        // ARRIVED
        else if(state == ApplicationStateManager.STATES.ARRIVED && !(act instanceof ArrivedActivity)){
            i = new Intent(getApplicationContext(), ArrivedActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    public SessionManager getSession(){
        return session;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
