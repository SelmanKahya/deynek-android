package com.deynek.app.activity.find;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.MainActivity;
import com.deynek.app.activity.mark.LeavingSpotActivity;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;
import com.google.android.gms.maps.model.LatLng;

public class ArrivedActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_arrived);
    }

    public void onNavigateClick(View v) {
        ParkInfoManager park = new ParkInfoManager();
        String loc = park.getParkingLocation();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc));
        startActivity(i);
        finish();
    }

    public void onParkedClick(View v) {
        ApplicationStateManager.saveState(ApplicationStateManager.STATES.DEFAULT);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void onFindAnotherClick(View v) {
        ApplicationStateManager.saveState(ApplicationStateManager.STATES.DEFAULT);
        Intent i = new Intent(getApplicationContext(), FindSpotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
