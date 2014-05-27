package com.deynek.app.activity.account;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.SettingsActivity;
import com.deynek.app.activity.find.FindActivity_NEW;
import com.deynek.app.activity.mark.MarkSpotActivity;
import com.deynek.app.model.MyActivity;

public class MyProfile extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_my_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Profile");
        actionBar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_logout:
                getSession().logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onMarkButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), MarkSpotActivity.class);
        startActivity(i);
    }

    public void onFindButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), FindActivity_NEW.class);
        startActivity(i);
    }
}
